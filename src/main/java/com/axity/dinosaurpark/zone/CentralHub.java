package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.RevenueRecord;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CentralHub implements ParkZone {
    private final String name;
    private final double souvenirPrice;
    private final double purchaseProbability;
    private final Set<Tourist> currentTourists = new HashSet<>();
   // private long revenueIdCounter = 20000;

    public CentralHub(String name, double souvenirPrice, double purchaseProbability) {
        this.name = name;
        this.souvenirPrice = souvenirPrice;
        this.purchaseProbability = purchaseProbability;
    }

    @Override
    public String getName() { return name; }
    @Override
    public boolean hasCapacity() { return true; } // Capacidad ilimitada
    @Override
    public int getCurrentOccupancy() { return currentTourists.size(); }
    @Override
    public int getMaxCapacity() { return Integer.MAX_VALUE; }

    @Override
    public void enter(Tourist tourist) { currentTourists.add(tourist); }
    @Override
    public void exit(Tourist tourist) { currentTourists.remove(tourist); }

    public void visit(Tourist tourist, Random random, DatabaseService dbService){
        enter(tourist);
        tourist.recordVisit(name);
        if (random.nextDouble() < purchaseProbability) {
            tourist.spend(souvenirPrice);
            dbService.saveRevenue(new RevenueRecord(DatabaseService.generateRevenueId(), "SOUVENIR", souvenirPrice, tourist.getId(), name, LocalDateTime.now()));
        }
        exit(tourist);
    }
}