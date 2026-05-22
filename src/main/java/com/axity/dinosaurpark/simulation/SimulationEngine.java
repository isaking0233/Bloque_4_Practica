package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.*;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.zone.*;
import com.axity.dinosaurpark.monitoring.ParkMonitor;
import com.axity.dinosaurpark.event.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationEngine {
    private final ParkConfig config;
    private final ParkState state;

    private final ArrivalZone arrivalZone;
    private final CentralHub centralHub;
    private final BathroomZone bathroomZone;
    private final ObservationEnclosure basicEnclosure;
    private final ObservationEnclosure premiumEnclosure;
    private final ObservationEnclosure vipEnclosure;

    public SimulationEngine(ParkConfig config) {
        this.config = config;

        // Se elimina config.getSeed() para garantizar el no-determinismo (Requerimiento)
        Random random = new Random();

        // Usamos base de datos relacional, la cual funciona en memoria y se destruye al acabar el programa
        // en lugar de usar csv
        DatabaseService dbService = new DatabaseService();

        List<Tourist> waitingTourists = new ArrayList<>();
        for (int i = 1; i <= config.getInt("tourists", 50); i++) {
            waitingTourists.add(new Tourist(i, "Turista-" + i));
        }

        List<Dinosaur> dinos = new ArrayList<>();
        for (int i = 1; i <= config.getInt("dinosaurs.carnivores", 5); i++) {
            dinos.add(new CarnivoreDinosaur(i, "Rex-" + i, "TRex"));
        }
        for (int i = 1; i <= config.getInt("dinosaurs.herbivores", 15); i++) {
            dinos.add(new HerbivoreDinosaur(i , "Tricera-" + i, "Triceratops"));
        }

        // Instanciamos vehiculos para los tecnicos
        List<Vehicle> vehicles = new ArrayList<>();
        int numTechnicians = config.getInt("workers.technicians", 2);
        for (int i = 1; i <= numTechnicians; i++) {
            vehicles.add(new Vehicle(i, "Jeep-T" + i));
        }

        List<Worker> workers = new ArrayList<>();
        double salary = config.getDouble("workers.dailySalary", 150.0);
        for (int i = 1; i <= config.getInt("workers.guards", 3); i++) {
            workers.add(new Guard(i, "Guardia-" + i, salary));
        }
        for (int i = 1; i <= numTechnicians; i++) {
            // Asignamos el vehículo correspondiente a cada tecnico
            workers.add(new Technician(i + 100, "Técnico-" + i, salary, vehicles.get(i - 1)));
        }

        PowerPlant powerPlant = new PowerPlant("Planta Principal",
                config.getDouble("powerplant.initialEnergy", 100.0),
                config.getDouble("powerplant.consumptionPerStep", 1.5),
                config.getDouble("powerplant.failureProbability", 0.05));

        arrivalZone = new ArrivalZone("Entrada", config.getInt("arrival.maxCapacity", 30), config.getDouble("arrival.ticketPrice", 25.0));
        waitingTourists.forEach(arrivalZone::enter);

        centralHub = new CentralHub("Hub Central", config.getDouble("hub.souvenirPrice", 15.0), config.getDouble("hub.souvenirPurchaseProbability", 0.4));
        bathroomZone = new BathroomZone("Baños", config.getInt("bathroom.maxCapacity", 10), config.getInt("bathroom.useDurationSteps", 3), config.getDouble("bathroom.spaPrice", 20.0), config.getDouble("bathroom.spaPurchaseProbability", 0.2));

        basicEnclosure = new ObservationEnclosure("Zona Básica", config.getInt("enclosure.basic.maxVisitors", 20), config.getDouble("enclosure.basic.entryFee", 10.0), ExperienceType.BASIC);
        premiumEnclosure = new ObservationEnclosure("Zona Premium", config.getInt("enclosure.premium.maxVisitors", 12), config.getDouble("enclosure.premium.entryFee", 30.0), ExperienceType.PREMIUM);
        vipEnclosure = new ObservationEnclosure("Zona VIP", config.getInt("enclosure.vip.maxVisitors", 5), config.getDouble("enclosure.vip.entryFee", 75.0), ExperienceType.VIP);

        // Actualizamos parametros del ParkState
        this.state = new ParkState(new ArrayList<>(), dinos, workers, vehicles, powerPlant, dbService, random);
    }

    public void run() {
        int totalSteps = config.getTotalSteps();
        int batchSize = config.getInt("simulation.arrivalBatchSize", 5);
        int maxRepairSteps = config.getInt("vehicles.repairsteps", 3); // Cargar pasos de reparacion

        for (int step = 0; step < totalSteps; step++) {
            state.incrementStep();
            state.clearActiveEvents(); // Limpiar eventos del turno anterior

            List<Tourist> arrived = arrivalZone.processBatch(batchSize, state.getDbService());
            state.getActiveTourists().addAll(arrived);

            for (Tourist tourist : state.getActiveTourists()) {
                if (tourist.getStatus() == TouristStatus.IN_PARK) {
                    centralHub.visit(tourist, state.getRandom(), state.getDbService());
                    bathroomZone.tryEnter(tourist, state.getRandom(), state.getDbService());

                    int rand = state.getRandom().nextInt(3);
                    if (rand == 0) basicEnclosure.visit(tourist, state.getRandom(), state.getDbService());
                    else if (rand == 1) premiumEnclosure.visit(tourist, state.getRandom(), state.getDbService());
                    else vipEnclosure.visit(tourist, state.getRandom(), state.getDbService());
                }
            }

            bathroomZone.tick();
            state.getPowerPlant().tick(state.getRandom(), state.getDbService());

            // Ejecucion de eventos aleatorios con sus respectivas probabilidades
            if (state.getRandom().nextDouble() < config.getDouble("event.storm.probability", 0.03)) {
                SimulationEvent storm = new StormEvent();
                storm.execute(state, state.getRandom());
                state.addActiveEvent(storm.getName());
            }

            if (state.getRandom().nextDouble() < config.getDouble("event.escape.probability", 0.02)) {
                SimulationEvent escape = new DinosaurEscapeEvent();
                escape.execute(state, state.getRandom());
                state.addActiveEvent(escape.getName());
            }

            if (state.getRandom().nextDouble() < config.getDouble("event.blackout.probability", 0.01)) {
                SimulationEvent blackout = new BlackoutEvent();
                blackout.execute(state, state.getRandom());
                state.addActiveEvent(blackout.getName());
            }

            if (state.getRandom().nextDouble() < config.getDouble("event.vehiclebreakdown.probability", 0.05)) {
                SimulationEvent breakdown = new VehicleBreakdownEvent();
                breakdown.execute(state, state.getRandom());
                state.addActiveEvent(breakdown.getName());
            }

            if (state.getRandom().nextDouble() < config.getDouble("event.promo.probability", 0.04)) {
                SimulationEvent promo = new DealsHourEvent();
                promo.execute(state, state.getRandom());
                state.addActiveEvent(promo.getName());
            }

            // Logica de vehiculos y trabajadores:
            // los vehículos se reparan automáticamente después de X pasos,
            // y los técnicos solo pueden reparar si tienen un vehículo disponible
            for (Vehicle v : state.getVehicles()) {
                v.updateTicks(maxRepairSteps); // El vehículo se recupera si está roto
            }

            for (Worker worker : state.getWorkers()) {
                if (worker instanceof Guard guard) {
                    guard.recaptureEscapedDinosaurs(state.getDinosaurs());
                } else if (worker instanceof Technician tech) {
                    // Validacion de que el tecnico tiene un vehiculo asignado
                    // y que este se encuentra disponible para poder reparar
                    if (tech.getVehicle() != null && tech.getVehicle().getStatus() == VehicleStatus.AVAILABLE) {
                        tech.repairIfNeeded(state.getPowerPlant());
                    }
                }
            }

            // Monitor se imprime solo cada X intervalos
            int interval = config.getInt("monitoring.intervalsteps", 5);
            if (state.getCurrentStep() % interval == 0) {
                ParkMonitor.displaySnapshot(state);
            }
        }

        // Imprimimos el reporte de las tablas antes de acabar la simulacion
        state.getDbService().printSummaryReport();
        // Al terminar los turnos, cerramos la conexion a la base de datos
        state.getDbService().close();
    }
}