package com.axity.dinosaurpark.model;

import java.time.LocalDateTime;

public class Ticket {
    private final long id;
    private final int touristId;
    private final double price;
    private final String category;
    private final LocalDateTime issuedAt;

    public Ticket(long id, int touristId, double price, String category, LocalDateTime issuedAt) {
        this.id = id;
        this.touristId = touristId;
        this.price = price;
        this.category = category;
        this.issuedAt = issuedAt;
    }

    // Solo getters
    // Una vez que se vende el boleto, no puede cambiar
    public long getId() { return id; }
    public int getTouristId() { return touristId; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
}