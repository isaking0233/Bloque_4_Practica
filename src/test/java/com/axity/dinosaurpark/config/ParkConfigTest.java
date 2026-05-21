package com.axity.dinosaurpark.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParkConfigTest {

    @Test
    void testSingletonInstance() {
        ParkConfig instance1 = ParkConfig.getInstance();
        ParkConfig instance2 = ParkConfig.getInstance();

        // Verifica que ambas variables apunten exactamente al mismo objeto en memoria
        assertSame(instance1, instance2, "El Singleton está creando múltiples instancias");
    }

    @Test
    void testDefaultValues() {
        ParkConfig config = ParkConfig.getInstance();

        // Si no encuentra la llave, debe devolver el valor por defecto que le pasamos
        assertEquals(999, config.getInt("llave.falsa", 999));
        assertEquals("default", config.getString("llave.falsa", "default"));
    }
}