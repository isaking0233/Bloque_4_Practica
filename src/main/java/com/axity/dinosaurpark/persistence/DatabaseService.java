package com.axity.dinosaurpark.persistence;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.SQLException;

public class DatabaseService {
    private static final String DB_URL = "jdbc:h2:mem:dinosaurparkdb;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private Connection connection;
    private static long globalRevenueId = 1;

    public static synchronized long generateRevenueId() {
        return globalRevenueId++;
    }

    public DatabaseService() {
        try {
            // Establecemos la conexion con H2 en memoria
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Iniciamos Liquibase para crear las tablas
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase("db/changelog/changelog-master.xml",
                    new ClassLoaderResourceAccessor(), database);

            liquibase.update("");
            System.out.println("¡Base de datos H2 e historial de Liquibase inicializados con éxito!");

        } catch (Exception e) {
            System.err.println("Error al inicializar la base de datos o Liquibase");
            e.printStackTrace();
        }
    }

    public void saveRevenue(RevenueRecord record) {
        String query = "INSERT INTO REVENUES (id, type, amount, tourist_id, zone_name, timestamp) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement queryRevnue = connection.prepareStatement(query)) {
            queryRevnue.setLong(1, record.id());
            queryRevnue.setString(2, record.type());
            queryRevnue.setDouble(3, record.amount());
            queryRevnue.setInt(4, record.touristId());
            queryRevnue.setString(5, record.zone());
            queryRevnue.setTimestamp(6, Timestamp.valueOf(record.timestamp()));
            queryRevnue.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void saveExpense(ExpenseRecord e) {
        String query = "INSERT INTO EXPENSES (id, type, amount, description, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement queryExpenses = connection.prepareStatement(query)) {
            queryExpenses.setLong(1, e.id());
            queryExpenses.setString(2, e.type());
            queryExpenses.setDouble(3, e.amount());
            queryExpenses.setString(4, e.description());
            queryExpenses.setTimestamp(5, Timestamp.valueOf(e.timestamp()));
            queryExpenses.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    public void saveEvent(EventRecord ev) {
        String query = "INSERT INTO EVENTS (step, event_name, description, affected_entities, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement queryEvents = connection.prepareStatement(query)) {
            queryEvents.setLong(1, ev.step());
            queryEvents.setString(2, ev.eventName());
            queryEvents.setString(3, ev.description());
            queryEvents.setString(4, ev.affectedEntities());
            queryEvents.setTimestamp(5, Timestamp.valueOf(ev.timestamp()));
            queryEvents.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Metodo para cerrar la conexión al finalizar la simulación
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void printSummaryReport() {
        System.out.println("\n=========================================================================================");
        System.out.println("                      REPORTES DE PERSISTENCIA EN BASE DE DATOS H2                     ");
        System.out.println("=========================================================================================");

        // Conteo total de eventos, gastos e ingresos
        try (var canalConsulta = connection.createStatement()) {
            var datosRevenues = canalConsulta.executeQuery("SELECT COUNT(*) FROM REVENUES");
            datosRevenues.next(); int totalRevenues = datosRevenues.getInt(1);

            var datosExpenses = canalConsulta.executeQuery("SELECT COUNT(*) FROM EXPENSES");
            datosExpenses.next(); int totalExpenses = datosExpenses.getInt(1);

            var datosEvents = canalConsulta.executeQuery("SELECT COUNT(*) FROM EVENTS");
            datosEvents.next(); int totalEvents = datosEvents.getInt(1);

            System.out.println(" [Métricas Globales]:");
            System.out.println("  -> Total registros en REVENUES : " + totalRevenues);
            System.out.println("  -> Total registros en EXPENSES : " + totalExpenses);
            System.out.println("  -> Total registros en EVENTS   : " + totalEvents);
        } catch (SQLException e) { e.printStackTrace(); }

        // Vemos los ingresos mas recientes
        System.out.println("\n-----------------------------------------------------------------------------------------");
        System.out.println("  ÚLTIMOS INGRESOS REGISTRADOS (TABLA: REVENUES)            ");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("| %-6s | %-15s | %-8s | %-10s | %-20s |\n", "ID", "TIPO", "MONTO", "TURISTA_ID", "ZONA");
        System.out.println("-----------------------------------------------------------------------------------------");

        String sqlRev = "SELECT id, type, amount, tourist_id, zone_name FROM REVENUES ORDER BY id DESC LIMIT 5";
        try (PreparedStatement psRevenues = connection.prepareStatement(sqlRev); var rsRevenues = psRevenues.executeQuery()) {
            while (rsRevenues.next()) {
                System.out.printf("| %-6d | %-15s | $%-7.2f | %-10d | %-20s |\n",
                        rsRevenues.getLong("id"), rsRevenues.getString("type"), rsRevenues.getDouble("amount"),
                        rsRevenues.getInt("tourist_id"), rsRevenues.getString("zone_name"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        System.out.println("-----------------------------------------------------------------------------------------");

        // Vemos los eventos mas recientes
        System.out.println("\n-----------------------------------------------------------------------------------------");
        System.out.println("  ÚLTIMOS EVENTOS REGISTRADOS (TABLA: EVENTS)               ");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("| %-6s | %-20s | %-45s |\n", "PASO", "EVENTO", "ENTIDADES AFECTADAS");
        System.out.println("-----------------------------------------------------------------------------------------");

        String sqlEv = "SELECT step, event_name, affected_entities FROM EVENTS ORDER BY step DESC LIMIT 5";
        try (PreparedStatement psEvents = connection.prepareStatement(sqlEv); var rsEvents = psEvents.executeQuery()) {
            while (rsEvents.next()) {
                System.out.printf("| %-6d | %-20s | %-45s |\n",
                        rsEvents.getLong("step"), rsEvents.getString("event_name"), rsEvents.getString("affected_entities"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("=========================================================================================\n");
    }
}