package com.axity.dinosaurpark.config;

import java.io.InputStream;
import java.util.Properties;

public final class ParkConfig {

    private static ParkConfig instance;
    private final Properties props;

    // Usamos Singleton para tener solo una instancia de ParkConfig en toda la aplicacion
    private ParkConfig() {
        props = new Properties();
        // Cargamos el archivo park.properties
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("park.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                System.err.println("No se encontró el archivo park.properties");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Punto de acceso global para obtener la configuración única del parque (Lazy Initialization)
    public static ParkConfig getInstance() {
        if (instance == null) {
            // Nadie puede instanciar ParkConfig directamente
            instance = new ParkConfig();
        }
        return instance;
    }

    // Metodos de lectura
    public int getInt(String key, int defaultValue) {
        String valor = props.getProperty(key);
        return valor != null ? Integer.parseInt(valor) : defaultValue;
    }

    public double getDouble(String key, double defaultValue) {
        String valor = props.getProperty(key);
        return valor != null ? Double.parseDouble(valor) : defaultValue;
    }

    public String getString(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public long getSeed() {
        return Long.parseLong(props.getProperty("simulation.seed", "42"));
    }

    public int getTotalSteps() {
        return getInt("simulation.totalSteps", 100);
    }

    // Solo para tests
    // Nos permite resetear la instancia entre pruebas
    static void resetForTesting() {
        instance = null;
    }
}