package com.axity.dinosaurpark.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DinosaurTest {

    @Test
    void testCarnivoreBehaviors() {
        Dinosaur rex = new CarnivoreDinosaur(1, "Rexy", "T-Rex");

        assertEquals(DinosaurStatus.IN_ENCLOSURE, rex.getStatus());
        assertEquals(0.9, rex.getDangerLevel());
        assertEquals("CARNIVORE", rex.getDiet());

        rex.escape();
        assertEquals(DinosaurStatus.ESCAPED, rex.getStatus());

        rex.returnToEnclosure();
        assertEquals(DinosaurStatus.IN_ENCLOSURE, rex.getStatus());
    }
}