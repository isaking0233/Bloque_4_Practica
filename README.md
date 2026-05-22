# Simulador de Parque de Dinosaurios (Bloque Intermedio)

Motor de simulación **no determinista (estocástico)** basado en turnos para la gestión de un parque de atracciones de dinosaurios. Este proyecto modela el comportamiento de turistas, trabajadores, recintos y eventos aleatorios utilizando principios sólidos de Programación Orientada a Objetos (POO), patrones de diseño y generación de probabilidades por ciclo.

## Tecnologías Utilizadas
* **Lenguaje:** Java 17
* **Gestor de Dependencias:** Maven
* **Base de Datos:** H2 (Archivo físico local)
* **Migraciones de BD:** Liquibase
* **Pruebas Unitarias e Integración:** JUnit 5 & Mockito
* **Cobertura de Código:** JaCoCo

## Arquitectura y Patrones de Diseño
El motor de simulación está diseñado priorizando la escalabilidad y el mantenimiento:

* **Singleton:** Utilizado en `ParkConfig` para garantizar una única instancia global de lectura del archivo `park.properties`.

```mermaid
classDiagram
    class ParkConfig {
        - static ParkConfig instance
        - Properties props
        - ParkConfig()
        + static ParkConfig getInstance()
        + int getInt(String key, int default)
        + double getDouble(String key, double default)
    }
    note for ParkConfig "Garantiza una única instancia, en memoria para las properties"
   ```

* **Strategy Pattern:** Implementado en el sistema de eventos (`SimulationEvent`). Permite la ejecución dinámica de eventos (Fugas, Tormentas, Apagones, Promociones y Fallas mecánicas) sin modificar el motor principal, en esta ocasion, la gestion ya no depende de un Scheduler fijo, sino de evaluacion de probabilidades directamente en el motor.

```mermaid
classDiagram
    class SimulationEvent {
        <<interface>>
        +String getName()
        +String getDescription()
        +void execute(ParkState state, Random rng)
    }
    class BlackoutEvent {
        +execute(ParkState state, Random rng)
    }
    class DinosaurEscapeEvent {
        +execute(ParkState state, Random rng)
    }
    class StormEvent {
        +execute(ParkState state, Random rng)
    }
    class VehicleBreakdownEvent {
        +execute(ParkState state, Random rng)
    }
    class DealsHourEvent {
        +execute(ParkState state, Random rng)
    }
    
    SimulationEvent <|.. BlackoutEvent
    SimulationEvent <|.. DinosaurEscapeEvent
    SimulationEvent <|.. StormEvent
    SimulationEvent <|.. VehicleBreakdownEvent
    SimulationEvent <|.. DealsHourEvent
    
    class SimulationEngine {
        +run()
    }
    SimulationEngine --> SimulationEvent : Inyecta e invoca por probabilidad
```

* **Polimorfismo:** Aplicado en las entidades del parque (como `Worker` delegando comportamientos a `Guard` y `Technician`).

## Características del Bloque Intermedio
1. **Persistencia Real:** Uso de base de datos relacional (H2) con generación automática del esquema vía Liquibase.
2. **Generación Segura de IDs:** Implementación de un Contador Maestro Sincronizado en `DatabaseService` para evitar colisiones de llaves primarias en transacciones concurrentes.
3. **Máquina de Estados en Vehículos:** Los técnicos dependen del estado operativo de los vehículos (AVAILABLE / BROKEN) para realizar mantenimientos en la planta de energía. Los vehículos averiados se recuperan automáticamente tras una cantidad definida de turnos.
4. **Nuevos Eventos Aleatorios:** Incorporación de DealsHourEvent (impacto económico y subsidios en entradas) y VehicleBreakdownEvent (desgaste de recursos físicos).
5. **Monitor por Intervalos:** Salida en consola de las 5 métricas principales del parque (Turistas, Dinosaurios, Energía, Eventos, Vehículos) regulada mediante módulo matemático de intervalos configurables.

## Instrucciones de Ejecución

### 1. Compilación y Pruebas (Cobertura > 65%)
Para ejecutar la suite de pruebas (unitarias y de integración del motor) y generar el reporte visual de JaCoCo verificando la cobertura del código (> 65%), ejecuta en la terminal:
```bash
mvn clean test jacoco:report
```

### 2. Ejecutar la Simulación Principal
Para arrancar el motor de simulación de 100 pasos, conectarse a la base de datos H2 y observar el comportamiento no determinista del parque, ejecuta:
```bash
mvn compile exec:java "-Dexec.mainClass=com.axity.dinosaurpark.Main"
```
