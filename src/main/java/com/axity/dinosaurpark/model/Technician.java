package com.axity.dinosaurpark.model;

import com.axity.dinosaurpark.zone.PowerPlant;

public class Technician extends Worker {
    private final Vehicle assignedVehicle;

    public Technician(int id, String name, double dailySalary, Vehicle assignedVehicle) {
        super(id, name, dailySalary);
        this.assignedVehicle = assignedVehicle;
    }

    public Vehicle getVehicle() {
        return assignedVehicle;
    }

    @Override
    public String getRole() { return "TECHNICIAN"; }

    public void repairIfNeeded(PowerPlant plant) {
        // Solo puede reparar si la planta esta fallando y su vehiculo esta funcionando
        if (!plant.isOperational() && assignedVehicle.getStatus() == VehicleStatus.AVAILABLE) {
            plant.repair();
            System.out.println("-- Técnico " + getName() + " reparó la planta usando el vehículo " + assignedVehicle.getModel());
        }
    }
}