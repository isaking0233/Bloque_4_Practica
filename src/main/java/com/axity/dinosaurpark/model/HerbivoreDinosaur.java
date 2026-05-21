package com.axity.dinosaurpark.model;

public class HerbivoreDinosaur extends Dinosaur {

    public HerbivoreDinosaur(int id, String name, String species) {
        // El costo de alimentacion es de 200 para herbívoros
        super(id, name, species, 200.0);
    }

    @Override
    public String getDiet() {
        return "HERBIVORE";
    }

    @Override
    public double getDangerLevel() {
        // Bajo peligro
        return 0.2;
    }
}