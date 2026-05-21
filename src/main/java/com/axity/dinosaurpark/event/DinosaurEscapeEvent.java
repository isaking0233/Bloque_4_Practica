package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class DinosaurEscapeEvent implements SimulationEvent {
    @Override
    public String getName() { return "ESCAPE_DINOSAURIO"; }

    @Override
    public String getDescription() { return "Un dinosaurio ha escapado de su celda."; }

    @Override
    public void execute(ParkState state, Random random) {
        List<Dinosaur> enclosedDinos = state.getDinosaurs().stream()
                .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE)
                .toList();

        if (enclosedDinos.isEmpty()) return;

        Dinosaur escapa = enclosedDinos.get(random.nextInt(enclosedDinos.size()));
        escapa.escape();
        String consecuencia = "Dinosaurio: " + escapa.getName();

        if (random.nextDouble() < escapa.getDangerLevel()) {
            List<Tourist> vulnerableTourists = state.getActiveTourists().stream()
                    .filter(t -> t.getStatus() == TouristStatus.IN_PARK)
                    .toList();

            if (!vulnerableTourists.isEmpty()) {
                Tourist victima = vulnerableTourists.get(random.nextInt(vulnerableTourists.size()));
                victima.setStatus(TouristStatus.ATTACKED);
                consecuencia += " | Turista atacado: " + victima.getName();
            }
        }

        // Guarda el evento en la Base de datos
        state.getDbService().saveEvent(new EventRecord(
                state.getCurrentStep(), getName(), getDescription(), consecuencia, LocalDateTime.now()
        ));
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Dinosaurios", LocalDateTime.now());
    }
}