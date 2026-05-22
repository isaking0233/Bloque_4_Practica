package com.axity.dinosaurpark.model;

public class Vehicle {
    private final int id;
    private final String model;
    private VehicleStatus status;
    // Contador de turnos para repararse
    private int repairTicksLeft = 0;

    public Vehicle(int id, String model) {
        this.id = id;
        this.model = model;
        this.status = VehicleStatus.AVAILABLE;
    }

    public int getId() { return id; }
    public String getModel() { return model; }
    public VehicleStatus getStatus() { return status; }
    public void setStatus(VehicleStatus status) { this.status = status; }

    public void breakVehicle() {
        this.status = VehicleStatus.BROKEN;
        // Reiniciamos el contador al averiarse
        this.repairTicksLeft = 0;
    }

    // Lógica para que se repare con el paso del tiempo
    public void updateTicks(int maxRepairSteps) {
        if (status == VehicleStatus.BROKEN) {
            repairTicksLeft++;
            if (repairTicksLeft >= maxRepairSteps) {
                status = VehicleStatus.AVAILABLE;
                // Vehículo reparado y listo para usarse
                repairTicksLeft = 0;
            }
        }
    }
}