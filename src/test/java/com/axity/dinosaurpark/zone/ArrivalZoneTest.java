package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.RevenueRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArrivalZoneTest {

    @Mock
    private DatabaseService mockDbService;

    private ArrivalZone arrivalZone;

    @BeforeEach
    void setUp() {
        // Configuramos la zona para un máximo de 2 personas a $50 el boleto
        arrivalZone = new ArrivalZone("Entrada Principal", 2, 50.0);
    }

    @Test
    void testProcessBatch_CaminoFeliz() {
        Tourist t1 = new Tourist(1, "Alan Grant");
        Tourist t2 = new Tourist(2, "Ellie Sattler");

        // 1. Los formamos en la línea de espera
        arrivalZone.enter(t1);
        arrivalZone.enter(t2);

        // 2. Procesamos un lote de hasta 5 personas
        List<Tourist> processed = arrivalZone.processBatch(5, mockDbService);

        // 3. Verificamos la lógica de tu metodo
        assertEquals(2, processed.size(), "Debió procesar exactamente a 2 turistas");
        assertEquals(2, arrivalZone.getCurrentOccupancy(), "El parque debería tener a 2 personas adentro");

        // Verificamos que al turista se le aplicaron los cambios correctos
        assertEquals(TouristStatus.IN_PARK, t1.getStatus());
        assertEquals(50.0, t1.getMoneySpent());
        assertTrue(t1.getVisitedZones().contains("Entrada Principal"));

        // Verificamos que se llamó a la base de datos exactamente 2 veces
        verify(mockDbService, times(2)).saveRevenue(any(RevenueRecord.class));
    }

    @Test
    void testProcessBatch_LimiteDeCapacidad() {
        // Formamos a 3 turistas, pero el parque solo admite a 2
        Tourist t1 = new Tourist(1, "Turista 1");
        Tourist t2 = new Tourist(2, "Turista 2");
        Tourist t3 = new Tourist(3, "Turista 3"); // El no entrara

        arrivalZone.enter(t1);
        arrivalZone.enter(t2);
        arrivalZone.enter(t3);

        List<Tourist> processed = arrivalZone.processBatch(5, mockDbService);

        // Verificamos funcionamiento del if (!hasCapacity()) break;
        assertEquals(2, processed.size(), "La capacidad máxima bloqueó la entrada del 3er turista");
        assertFalse(arrivalZone.hasCapacity(), "El parque debería estar reportado como lleno");

        // El turista 3 permanece con su dinero y estado original
        assertEquals(TouristStatus.WAITING, t3.getStatus());
        assertEquals(0.0, t3.getMoneySpent());
    }
}