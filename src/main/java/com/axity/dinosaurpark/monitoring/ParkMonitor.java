package com.axity.dinosaurpark.monitoring;

import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.simulation.ParkState;

public class ParkMonitor {
    public static void displaySnapshot(ParkState state) {
        long activeTourists = state.getActiveTourists().stream()
                .filter(t -> t.getStatus() == TouristStatus.IN_PARK).count();
        long enclosedDinos = state.getDinosaurs().stream()
                .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE).count();

        System.out.println("=== Step " + state.getCurrentStep() + " ===");
        System.out.println("Turistas activos: " + activeTourists);
        System.out.println("Dinosaurios en encierro: " + enclosedDinos);
        System.out.println("Energía de planta: " + String.format("%.1f", state.getPowerPlant().getEnergyLevel()) + "%");
        System.out.println("-------------------------");
    }
}