package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.RevenueRecord;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BathroomZone implements ParkZone {
    private final String name;
    private final int maxCapacity;
    private final int useDurationSteps;
    private final double spaPrice;
    private final double spaProbability;
    private final Map<Tourist, Integer> slots = new HashMap<>();

    // private long revenueIdCounter = 20000;

    public BathroomZone(String name, int maxCapacity, int useDurationSteps, double spaPrice, double spaProbability) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.useDurationSteps = useDurationSteps;
        this.spaPrice = spaPrice;
        this.spaProbability = spaProbability;
    }

    @Override
    public String getName() { return name; }
    @Override
    public boolean hasCapacity() { return slots.size() < maxCapacity; }
    @Override
    public int getCurrentOccupancy() { return slots.size(); }
    @Override
    public int getMaxCapacity() { return maxCapacity; }

    @Override
    public void enter(Tourist tourist) { slots.put(tourist, useDurationSteps); }
    @Override
    public void exit(Tourist tourist) { slots.remove(tourist); }

    public boolean tryEnter(Tourist tourist, Random random, DatabaseService dbService) {
        if (hasCapacity()) {
            enter(tourist);
            tourist.recordVisit(name);
            if (random.nextDouble() < spaProbability) {
                tourist.spend(spaPrice);
                dbService.saveRevenue(new RevenueRecord(DatabaseService.generateRevenueId(), "SPA", spaPrice, tourist.getId(), name, LocalDateTime.now()));
            }
            return true;
        }
        return false;
    }

    public void tick() {
        slots.replaceAll((tourist, stepsLeft) -> stepsLeft - 1);
        slots.entrySet().removeIf(entry -> entry.getValue() <= 0);
    }
}