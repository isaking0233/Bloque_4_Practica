package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.simulation.ParkState;
import java.time.LocalDateTime;
import java.util.Random;

public class BlackoutEvent implements SimulationEvent {
    @Override
    public String getName() { return "APAGON_MASIVO"; }

    @Override
    public String getDescription() { return "Falla total de la planta de energía."; }

    @Override
    public void execute(ParkState state, Random rng) {
        // Forzamos la caida del sistema electrico del parque
        state.getPowerPlant().triggerFailure(state.getDbService());

        // Guardamos las perdidas financieras asociadas en la Base de datos
        state.getDbService().saveExpense(new ExpenseRecord(
                state.generateExpenseId(), "BLACKOUT_DAMAGE", 2000.0, "Daños por apagon masivo", LocalDateTime.now()
        ));

        // Registra el evento en la Base de datos
        state.getDbService().saveEvent(new EventRecord(
                state.getCurrentStep(), getName(), getDescription(), "Planta de Energía", LocalDateTime.now()
        ));
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Planta de Energía", LocalDateTime.now());
    }
}