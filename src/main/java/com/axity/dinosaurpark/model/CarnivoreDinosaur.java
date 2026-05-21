package com.axity.dinosaurpark.model;

public class CarnivoreDinosaur extends Dinosaur {

    public CarnivoreDinosaur(int id, String name, String species) {
        // El costo de alimentacion es de 500 para carnivoros
        super(id, name, species, 500.0);
    }

    @Override
    public String getDiet() {
        return "CARNIVORE";
    }

    @Override
    public double getDangerLevel() {
        // Alto peligro
        return 0.9;
    }
}