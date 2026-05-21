package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

public record EventRecord(
        long step,
        String eventName,
        String description,
        String affectedEntities,
        LocalDateTime timestamp
) {
   /* public String toCsvLine() {
        return step + "," + eventName + "," + description + "," + affectedEntities + "," + timestamp;
    }

    No es necesario, porque estamos usando H2
    para eso esta DatabaseService

    */
}