package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;
import java.util.Random;

// Usamos Strategy para que todos los eventos usen la interfza
public interface SimulationEvent {
    String getName();
    String getDescription();
    void execute(ParkState state, Random random);
    EventRecord toRecord(long step);
}