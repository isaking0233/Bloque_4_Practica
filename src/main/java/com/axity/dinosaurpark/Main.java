package com.axity.dinosaurpark;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.simulation.SimulationEngine;

public class Main {
    public static void main(String[] args) {
        System.out.println("====== INICIANDO SIMULACION DEL PARQUE ======");
        ParkConfig config = ParkConfig.getInstance();
        SimulationEngine engine = new SimulationEngine(config);

        engine.run();

        System.out.println("====== SIMULACION TERMINADA CON EXITO ======");
    }
}