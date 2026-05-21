package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class EventScheduler {
    private final Map<Integer, SimulationEvent> scheduledEvents;

    public EventScheduler(long seed, int totalSteps) {
        scheduledEvents = new HashMap<>();
        Random random = new Random(seed);

        // Configuramos los eventos de forma aleatoria, sin solaparse
        scheduleEvent(new DinosaurEscapeEvent(), totalSteps, random);
        scheduleEvent(new BlackoutEvent(), totalSteps, random);
        scheduleEvent(new StormEvent(), totalSteps, random);
        scheduleEvent(new VehicleBreakdownEvent(), totalSteps, random);
        scheduleEvent(new PromoHourEvent(), totalSteps, random);
    }

    private void scheduleEvent(SimulationEvent event, int totalSteps, Random random) {
        int randomStep;
        do {
            randomStep = random.nextInt(totalSteps);
        } while (scheduledEvents.containsKey(randomStep)); // Evito que 2 eventos caigan en el mismo turno
        scheduledEvents.put(randomStep, event);
    }

    public Optional<SimulationEvent> checkForEvent(int step) {
        return Optional.ofNullable(scheduledEvents.get(step));
    }
}