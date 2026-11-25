package com.parking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests verifying behavior of Vehicle subclasses (Car and Motorcycle)
 * including area-based fee calculations and occupied area definitions.
 */
public class VehicleTest {

    @Test
    public void testCarAreaAndFeeCalculation() {
        // Car occupies 10 m²; rate is 2.0 TL / m² / hour -> 20.0 TL / hour
        Car car = new Car("34ABC01", 3);
        assertEquals(10, car.getOccupiedArea());
        assertEquals(60.0, car.calculateFee()); // 3h * 10 m² * 2.0 TL = 60.0 TL

        Car zeroHours = new Car("34ABC02", 0);
        assertEquals(0.0, zeroHours.calculateFee());
    }

    @Test
    public void testMotorcycleAreaAndFeeCalculation() {
        // Motorcycle occupies 5 m²; rate is 2.0 TL / m² / hour -> 10.0 TL / hour
        Motorcycle moto = new Motorcycle("34MOTO01", 4);
        assertEquals(5, moto.getOccupiedArea());
        assertEquals(40.0, moto.calculateFee()); // 4h * 5 m² * 2.0 TL = 40.0 TL
    }

    @Test
    public void testToStringContainsOccupiedArea() {
        Car car = new Car("34TEST01", 2);
        String output = car.toString();
        assertTrue(output.contains("10 m²"), "toString should include occupied area in m²");
        assertTrue(output.contains("34TEST01"), "toString should include plate number");
    }

    @Test
    public void testInvalidConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new Car("", 2));
        assertThrows(IllegalArgumentException.class, () -> new Car(null, 2));
        assertThrows(IllegalArgumentException.class, () -> new Motorcycle("34MOTO02", -1));
    }
}
