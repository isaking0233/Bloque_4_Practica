package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.RevenueRecord;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArrivalZone implements ParkZone {
    private final String name;
    private final int maxCapacity;
    private final double ticketPrice;
    private final Queue<Tourist> waitingLine;
    private final List<Tourist> insidePark;
  //  private long revenueIdCounter = 10000;

    public ArrivalZone(String name, int maxCapacity, double ticketPrice) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.ticketPrice = ticketPrice;
        this.waitingLine = new LinkedList<>();
        this.insidePark = new LinkedList<>();
    }

    @Override
    public String getName() { return name; }
    @Override
    public boolean hasCapacity() { return insidePark.size() < maxCapacity; }
    @Override
    public int getCurrentOccupancy() { return insidePark.size(); }
    @Override
    public int getMaxCapacity() { return maxCapacity; }

    @Override
    public void enter(Tourist tourist) { waitingLine.add(tourist); }
    @Override
    public void exit(Tourist tourist) {
        insidePark.remove(tourist);
        tourist.setStatus(TouristStatus.EXITED);
    }

    public List<Tourist> processBatch(int batchSize, DatabaseService dbService) {
        List<Tourist> processed = new LinkedList<>();
        for (int i = 0; i < batchSize; i++) {
            if (waitingLine.isEmpty() || !hasCapacity()) break;
            Tourist tourist = waitingLine.poll();
            tourist.setStatus(TouristStatus.IN_PARK);
            tourist.spend(ticketPrice);
            tourist.recordVisit(name);
            insidePark.add(tourist);
            processed.add(tourist);

            dbService.saveRevenue(new RevenueRecord(DatabaseService.generateRevenueId(), "TICKET", ticketPrice, tourist.getId(), name, LocalDateTime.now()));     }
        return processed;
    }

    public List<Tourist> getInsidePark() { return insidePark; }
}