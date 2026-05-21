package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.persistence.DatabaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PowerPlantTest {

    @Mock
    private DatabaseService mockDbService;

    @Test
    void testTriggerFailureAndRepair() {
        PowerPlant plant = new PowerPlant("Planta 1", 100.0, 1.5, 0.05);

        assertTrue(plant.isOperational());

        // Simulamos la falla pasándole el mock de la base de datos
        plant.triggerFailure(mockDbService);

        assertFalse(plant.isOperational());
        assertEquals(0.0, plant.getEnergyLevel());

        // Verificamos que la planta intenta guardar el gasto en H2
        verify(mockDbService).saveExpense(any());

        // Probamos la reparacion
        plant.repair();
        assertTrue(plant.isOperational());
        assertEquals(100.0, plant.getEnergyLevel());
    }
}