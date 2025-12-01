import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VehicleTest {

    private Vehicle vehicle;
    private SimpleDateFormat dateFormat;

    @BeforeEach
    void setUp() throws Exception {
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date manufactureDate = dateFormat.parse("15.05.2020");
        vehicle = new Vehicle("V001", "суша", "Toyota Camry", 180, 220, manufactureDate, 25000);
    }

    @Test
    void testVehicleCreation() {
        assertNotNull(vehicle);
        assertEquals("V001", vehicle.getId());
        assertEquals("суша", vehicle.getType());
        assertEquals("Toyota Camry", vehicle.getModel());
        assertEquals(180, vehicle.getEnginePower());
        assertEquals(220, vehicle.getMaxSpeed());
        assertEquals(25000, vehicle.getPrice());
    }

    @Test
    void testSetters() {
        vehicle.setType("вода");
        vehicle.setModel("Yamaha");
        vehicle.setEnginePower(200);
        vehicle.setMaxSpeed(250);
        vehicle.setPrice(30000);

        assertEquals("вода", vehicle.getType());
        assertEquals("Yamaha", vehicle.getModel());
        assertEquals(200, vehicle.getEnginePower());
        assertEquals(250, vehicle.getMaxSpeed());
        assertEquals(30000, vehicle.getPrice());
    }

    @Test
    void testEqualsAndHashCode() throws Exception {
        Date date1 = dateFormat.parse("15.05.2020");
        Date date2 = dateFormat.parse("20.10.2018");

        Vehicle vehicle1 = new Vehicle("V001", "суша", "Toyota", 180, 220, date1, 25000);
        Vehicle vehicle2 = new Vehicle("V001", "вода", "Yamaha", 200, 250, date2, 30000);
        Vehicle vehicle3 = new Vehicle("V002", "суша", "Toyota", 180, 220, date1, 25000);

        assertEquals(vehicle1, vehicle2);
        assertNotEquals(vehicle1, vehicle3);
        assertEquals(vehicle1.hashCode(), vehicle2.hashCode());
    }

    @Test
    void testToString() {
        String result = vehicle.toString();
        assertTrue(result.contains("V001"));
        assertTrue(result.contains("Toyota Camry"));
        assertTrue(result.contains("суша"));
        assertTrue(result.contains("180"));
        assertTrue(result.contains("220"));
        assertTrue(result.contains("25000"));
    }
}