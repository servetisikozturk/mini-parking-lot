package com.parking.factory;

import com.parking.model.Car;
import com.parking.model.Motorcycle;
import com.parking.model.Vehicle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for VehicleFactory.
 */
public class VehicleFactoryTest {

    @Test
    public void testCreateCar() {
        Vehicle vehicle = VehicleFactory.createVehicle("CAR", "34TEST01", 3);
        assertNotNull(vehicle);
        assertTrue(vehicle instanceof Car);
        assertEquals("34TEST01", vehicle.getPlateNumber());
        assertEquals(3, vehicle.getHoursParked());
    }

    @Test
    public void testCreateMotorcycle() {
        Vehicle vehicle = VehicleFactory.createVehicle("MOTORCYCLE", "34TEST02", 2);
        assertNotNull(vehicle);
        assertTrue(vehicle instanceof Motorcycle);
        assertEquals("34TEST02", vehicle.getPlateNumber());
        assertEquals(2, vehicle.getHoursParked());
    }

    @Test
    public void testInvalidTypeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            VehicleFactory.createVehicle("BUS", "34BUS01", 1)
        );
    }
}
