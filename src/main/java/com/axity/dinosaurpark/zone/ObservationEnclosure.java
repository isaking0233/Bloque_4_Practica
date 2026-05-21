package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.SatisfactionSurvey;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.RevenueRecord;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ObservationEnclosure implements ParkZone {
    private final String name;
    private final int maxCapacity;
    private final double entryFee;
    private final ExperienceType type;
    private final Set<Tourist> currentVisitors = new HashSet<>();
    private final List<SatisfactionSurvey> surveys = new ArrayList<>();
 // private static long revenueIdCounter = 4000;

    public ObservationEnclosure(String name, int maxCapacity, double entryFee, ExperienceType type) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.entryFee = entryFee;
        this.type = type;
    }

    @Override
    public String getName() { return name; }
    @Override
    public boolean hasCapacity() { return currentVisitors.size() < maxCapacity; }
    @Override
    public int getCurrentOccupancy() { return currentVisitors.size(); }
    @Override
    public int getMaxCapacity() { return maxCapacity; }

    @Override
    public void enter(Tourist tourist) { currentVisitors.add(tourist); }
    @Override
    public void exit(Tourist tourist) { currentVisitors.remove(tourist); }

    public void visit(Tourist tourist, Random random, DatabaseService dbService){
        if (hasCapacity()) {
            enter(tourist);
            tourist.recordVisit(name);
            tourist.spend(entryFee);

            dbService.saveRevenue(new RevenueRecord(DatabaseService.generateRevenueId(), "ENCLOSURE_ENTRY", entryFee, tourist.getId(), name, LocalDateTime.now()));

            conductSurvey(tourist, random);
            exit(tourist);
        }
    }

    // Puntuación por tipo
    // BASIC (de 1 a 3), PREMIUM (de 2 a 4), VIP (de 3 a 5)
    public void conductSurvey(Tourist tourist, Random random) {
        int score = 1;
        switch (type) {
            case BASIC -> score = 1 + random.nextInt(3);
            case PREMIUM -> score = 2 + random.nextInt(3);
            case VIP -> score = 3 + random.nextInt(3);
        }
        surveys.add(new SatisfactionSurvey(tourist.getId(), name, score));
    }

    public List<SatisfactionSurvey> getSurveys() { return surveys; }
}