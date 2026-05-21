package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

public record RevenueRecord(
        long id,
        String type,
        double amount,
        int touristId,
        String zone,
        LocalDateTime timestamp
) {

    /*public String toCsvLine() {
        return id + "," + type + "," + amount + "," + touristId + "," + zone + "," + timestamp;
    }

    No es necesario, porque estamos usando H2
    para eso esta DatabaseService

    */
}