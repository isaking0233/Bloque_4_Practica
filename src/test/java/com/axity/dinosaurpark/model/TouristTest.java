package com.axity.dinosaurpark.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TouristTest {

    @Test
    void testInitialStateAndSpend() {
        Tourist tourist = new Tourist(1, "Prueba");

        assertEquals(TouristStatus.WAITING, tourist.getStatus());
        assertEquals(0.0, tourist.getMoneySpent());

        tourist.spend(50.5);
        tourist.spend(10.0);

        assertEquals(60.5, tourist.getMoneySpent(), "El turista no está acumulando los gastos correctamente");
    }

    @Test
    void testRecordVisit() {
        Tourist tourist = new Tourist(1, "Prueba");
        tourist.recordVisit("Baños");

        assertTrue(tourist.getVisitedZones().contains("Baños"));
        assertEquals(1, tourist.getVisitedZones().size());
    }
}