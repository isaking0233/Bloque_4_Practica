package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.config.ParkConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SimulationEngineTest {

    @Test
    void testSimulationRunsCompleteCycleWithoutCrashing() {
        // Instanciamos la configuración global
        ParkConfig config = ParkConfig.getInstance();

        // Instanciamos el motor principal
        SimulationEngine engine = new SimulationEngine(config);

        // Al ejecutar el engine.run(), JaCoCo registrará que pasamos por
        // casi todas las líneas de código de zonas, modelos y persistencia.
        // Verificamos que la simulación entera corra sin lanzar errores.
        assertDoesNotThrow(() -> engine.run(),
                "El motor de simulación debería ejecutar todos los turnos sin excepciones.");
    }
}