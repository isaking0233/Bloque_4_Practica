package com.axity.dinosaurpark.model;

public class Vehicle {
    private final int id;
    private final String model;
    private VehicleStatus status;

    public Vehicle(int id, String model) {
        this.id = id;
        this.model = model;
        this.status = VehicleStatus.OPERATIONAL;
    }

    public int getId() { return id; }
    public String getModel() { return model; }
    public VehicleStatus getStatus() { return status; }
    public void setStatus(VehicleStatus status) { this.status = status; }
}