package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import java.time.LocalDateTime;
import java.util.Random;

public class PowerPlant implements ParkZone {
    private final String name;
    private double energyLevel;
    private final double consumptionPerStep;
    private final double failureProbability;
    private boolean operational;
    private long expenseIdCounter = 9000;

    public PowerPlant(String name, double initialEnergy, double consumptionPerStep, double failureProbability) {
        this.name = name;
        this.energyLevel = initialEnergy;
        this.consumptionPerStep = consumptionPerStep;
        this.failureProbability = failureProbability;
        this.operational = true;
    }

    @Override
    public String getName() { return name; }
    @Override
    public boolean hasCapacity() { return false; } // Área restringida
    @Override
    public int getCurrentOccupancy() { return 0; }
    @Override
    public int getMaxCapacity() { return 0; }
    @Override
    public void enter(Tourist tourist) {}
    @Override
    public void exit(Tourist tourist) {}

    public boolean isOperational() { return operational; }
    public double getEnergyLevel() { return energyLevel; }

    public void tick(Random random, DatabaseService dbService) {
        if (operational) {
            energyLevel = Math.max(0, energyLevel - consumptionPerStep);
            if (energyLevel <= 0 || random.nextDouble() < failureProbability) {
                triggerFailure(dbService);
            }
        }
    }

    public void triggerFailure(DatabaseService dbService) {
        this.operational = false;
        this.energyLevel = 0.0;
        dbService.saveExpense(new ExpenseRecord(
                expenseIdCounter++, "POWER_FAILURE_SYSTEM", 0, "Falla crítica en el sistema eléctrico", LocalDateTime.now()
        ));
    }

    public void repair() {
        this.operational = true;
        this.energyLevel = 100.0;
    }
}