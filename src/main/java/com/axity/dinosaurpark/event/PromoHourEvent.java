package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.RevenueRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class PromoHourEvent implements SimulationEvent {

    @Override
    public String getName() { return "HORA_DE_OFERTAS"; }

    @Override
    public String getDescription() { return "Promoción relámpago; aumentan las ventas de souvenirs."; }

    @Override
    public void execute(ParkState state, Random random) {
        List<Tourist> inPark = state.getActiveTourists().stream()
                .filter(tourist -> tourist.getStatus() == TouristStatus.IN_PARK)
                .toList();

        int buyersCount = 0;
        for (Tourist tourist : inPark) {
            // Probabilidad del 50% de que cada turista compre la oferta
            if (random.nextDouble() < 0.5) {
                tourist.spend(20.0);
                state.getDbService().saveRevenue(new RevenueRecord(DatabaseService.generateRevenueId(), "PROMO_SALE", 20.0, tourist.getId(), "Todo el Parque", LocalDateTime.now()));
                buyersCount++;
            }
        }

        state.getDbService().saveEvent(new EventRecord(
                state.getCurrentStep(), getName(), getDescription(), "Compradores: " + buyersCount, LocalDateTime.now()
        ));
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Turistas", LocalDateTime.now());
    }
}