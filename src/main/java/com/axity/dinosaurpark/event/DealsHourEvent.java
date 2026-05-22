package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.Random;

public class DealsHourEvent implements SimulationEvent {
    @Override
    public String getName() { return "DEALS_HOUR"; }

    @Override
    public String getDescription() { return "¡Descuento relámpago del 30% activado en el parque!"; }

    @Override
    public void execute(ParkState state, Random random) {
        // Obtenemos el precio base del boleto desde la configuracion
        double ticketPrice = ParkConfig.getInstance().getDouble("arrival.ticketPrice", 25.0);

        // Calculamos exactamente el 30% de descuento
        double discountPerTourist = ticketPrice * 0.30;

        // Vemos a cuantos turistas les aplica en este step
        int activeTouristsCount = state.getActiveTourists().size();
        double totalPromoCost = discountPerTourist * activeTouristsCount;

        // Registramos el impacto financiero del 30% en la base de datos
        if (activeTouristsCount > 0) {
            state.getDbService().saveExpense(new ExpenseRecord(
                    state.generateExpenseId(),
                    "PROMO_DISCOUNT",
                    totalPromoCost,
                    "Subsidio promocional del 30% aplicado a " + activeTouristsCount + " turistas",
                    LocalDateTime.now()
            ));
        }

        // Registramos la ocurrencia del evento
        state.getDbService().saveEvent(new EventRecord(
                state.getCurrentStep(), getName(), getDescription(), "Finanzas", LocalDateTime.now()
        ));
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Finanzas", LocalDateTime.now());
    }
}