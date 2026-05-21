package com.axity.dinosaurpark.model;

import java.util.ArrayList;
import java.util.List;

public class Tourist {
    private final int id;
    private final String name;
    private TouristStatus status;
    private double moneySpent;
    private final List<String> visitedZones;

    public Tourist(int id, String name) {
        this.id = id;
        this.name = name;
        this.status = TouristStatus.WAITING;
        // Estado inicial
        this.moneySpent = 0.0;
        this.visitedZones = new ArrayList<>();
    }

    // Metodos para gastar dinero y registrar visitas
    public void spend(double amount) {
        this.moneySpent += amount;
    }

    public void recordVisit(String zoneName) {
        this.visitedZones.add(zoneName);
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public TouristStatus getStatus() { return status; }
    public void setStatus(TouristStatus status) { this.status = status; }
    public double getMoneySpent() { return moneySpent; }
    public List<String> getVisitedZones() { return visitedZones; }
}