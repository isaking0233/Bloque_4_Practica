package com.axity.dinosaurpark.model;

import java.util.List;

public class Guard extends Worker {

    public Guard(int id, String name, double dailySalary) {
        super(id, name, dailySalary);
    }

    @Override
    public String getRole() { return "GUARD"; }

    // Metodo para recapturar dinosaurios que escaparon
    public void recaptureEscapedDinosaurs(List<Dinosaur> dinosaurs) {
        for (Dinosaur dino : dinosaurs) {
            if (dino.getStatus() == DinosaurStatus.ESCAPED) {
                dino.returnToEnclosure();
            }
        }
    }
}