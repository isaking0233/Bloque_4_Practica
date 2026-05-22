package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.Worker;
import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.zone.PowerPlant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParkState {
    private final List<Tourist> activeTourists;
    private final List<Dinosaur> dinosaurs;
    private final List<Worker> workers;
    private final List<Vehicle> vehicles;
    private final PowerPlant powerPlant;
    private final DatabaseService dbService;
    private final Random random;
    private int currentStep = 0;
    private long expenseIdCounter = 8000;

    // Lista para que el monitor sepa cuántos eventos ocurrieron en este turno
    private final List<String> activeEvents = new ArrayList<>();

    public ParkState(List<Tourist> activeTourists, List<Dinosaur> dinosaurs, List<Worker> workers,
                     List<Vehicle> vehicles, PowerPlant powerPlant, DatabaseService dbService, Random random) {
        this.activeTourists = activeTourists;
        this.dinosaurs = dinosaurs;
        this.workers = workers;
        this.vehicles = vehicles;
        this.powerPlant = powerPlant;
        this.dbService = dbService;
        this.random = random;
    }

    public void incrementStep() { currentStep++; }
    public int getCurrentStep() { return currentStep; }
    public List<Tourist> getActiveTourists() { return activeTourists; }
    public List<Dinosaur> getDinosaurs() { return dinosaurs; }
    public List<Worker> getWorkers() { return workers; }
    public List<Vehicle> getVehicles() { return vehicles; }
    public PowerPlant getPowerPlant() { return powerPlant; }
    public DatabaseService getDbService() { return dbService; }
    public Random getRandom() { return random; }
    public long generateExpenseId() { return expenseIdCounter++; }

    // Métodos para el sistema de eventos y el monitor
    public List<String> getActiveEvents() { return activeEvents; }
    public void clearActiveEvents() { activeEvents.clear(); }
    public void addActiveEvent(String eventName) { activeEvents.add(eventName); }
}