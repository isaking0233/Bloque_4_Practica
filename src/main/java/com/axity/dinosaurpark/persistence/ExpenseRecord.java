package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

public record ExpenseRecord(
        long id,
        String type,
        double amount,
        String description,
        LocalDateTime timestamp
) {
    /*
    public String toCsvLine() {
        return id + "," + type + "," + amount + "," + description + "," + timestamp;
    }

    No es necesario, porque estamos usando H2
    para eso esta DatabaseService

    */
}