package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.model.VehicleStatus;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class VehicleBreakdownEvent implements SimulationEvent {
    @Override
    public String getName() { return "FALLA_VEHICULO"; }

    @Override
    public String getDescription() { return "Un vehículo de mantenimiento se ha averiado en el parque."; }

    @Override
    public void execute(ParkState state, Random random) {
        // Obtenemos solo las unidades de transporte que se encuentran activas
        List<Vehicle> operational = state.getVehicles().stream()
                .filter(vehicle -> vehicle.getStatus() == VehicleStatus.OPERATIONAL)
                .toList();

        if (operational.isEmpty()) return; // Si no hay operativos, no pasa nada

        // Escogemos uno al azar y lo estropeamos
        Vehicle broken = operational.get(random.nextInt(operational.size()));
        broken.setStatus(VehicleStatus.BROKEN);

        // Gneramos gastos de reparacion
        state.getDbService().saveExpense(new ExpenseRecord(
                state.generateExpenseId(), "VEHICLE_REPAIR", 300.0, "Reparación de " + broken.getModel(), LocalDateTime.now()
        ));

        // Registramos el evento
        state.getDbService().saveEvent(new EventRecord(
                state.getCurrentStep(), getName(), getDescription(), "Afectado: " + broken.getModel(), LocalDateTime.now()
        ));
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Vehículos", LocalDateTime.now());
    }
}