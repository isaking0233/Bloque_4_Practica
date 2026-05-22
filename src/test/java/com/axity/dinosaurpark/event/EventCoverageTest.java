package com.axity.dinosaurpark.event;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventCoverageTest {

    @Test
    void testEventsInitializeCorrectly() {
        SimulationEvent storm = new StormEvent();
        SimulationEvent escape = new DinosaurEscapeEvent();
        SimulationEvent blackout = new BlackoutEvent();
        SimulationEvent breakdown = new VehicleBreakdownEvent();
        SimulationEvent promo = new DealsHourEvent();

        // Verificamos que los nombres no sean nulos para sumar cobertura
        assertNotNull(storm.getName());
        assertNotNull(escape.getName());
        assertNotNull(blackout.getName());
        assertNotNull(breakdown.getName());
        assertNotNull(promo.getName());

        // Verificamos las descripciones
        assertNotNull(storm.getDescription());
        assertNotNull(promo.getDescription());
    }
}