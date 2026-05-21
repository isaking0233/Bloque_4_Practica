package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.simulation.ParkState;
import java.time.LocalDateTime;
import java.util.Random;

public class StormEvent implements SimulationEvent {
    @Override
    public String getName() { return "TORMENTA_TORRENCIAL"; }

    @Override
    public String getDescription() { return "Tormenta severa obliga a evacuar visitantes en áreas abiertas."; }

    @Override
    public void execute(ParkState state, Random random) {
        for (Tourist turista : state.getActiveTourists()) {
            if (turista.getStatus() == TouristStatus.IN_PARK) {
                turista.recordVisit("Evacuación");
            }
        }
        // Simulamos el impacto de la tormenta aplicando cargos de mantenimiento por daños
        state.getDbService().saveExpense(new ExpenseRecord(
                state.generateExpenseId(), "STORM_DAMAGE", 500.0, "Daños por tormenta", LocalDateTime.now()
        ));

        state.getDbService().saveEvent(new EventRecord(
                state.getCurrentStep(), getName(), getDescription(), "Todos los turistas activos", LocalDateTime.now()
        ));
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Turistas", LocalDateTime.now());
    }
}