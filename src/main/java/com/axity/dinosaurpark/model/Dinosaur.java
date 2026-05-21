package com.axity.dinosaurpark.model;

public abstract class Dinosaur {
    private final int id;
    private final String name;
    private final String species;
    private DinosaurStatus status;
    private final double feedingCostPerDay;

    public Dinosaur(int id, String name, String species, double feedingCostPerDay) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.feedingCostPerDay = feedingCostPerDay;
        // Inicia en el encierro
        this.status = DinosaurStatus.IN_ENCLOSURE;
    }

    // Metodos abstractos que las subclases deben definir
    public abstract String getDiet();
    public abstract double getDangerLevel();

    // Metodos para cambio de estado
    public void escape() { this.status = DinosaurStatus.ESCAPED; }
    public void recapture() { this.status = DinosaurStatus.RECAPTURED; }
    public void returnToEnclosure() { this.status = DinosaurStatus.IN_ENCLOSURE; }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public DinosaurStatus getStatus() { return status; }
    public double getFeedingCostPerDay() { return feedingCostPerDay; }
}