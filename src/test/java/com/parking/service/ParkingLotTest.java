package com.parking.service;

import com.parking.exception.ParkingLotFullException;
import com.parking.exception.VehicleAlreadyParkedException;
import com.parking.exception.VehicleNotFoundException;
import com.parking.model.Car;
import com.parking.model.Motorcycle;
import com.parking.model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for business operations and capacity constraints in ParkingLot.
 */
public class ParkingLotTest {

    private ParkingLot lot;

    @BeforeEach
    public void setUp() {
        lot = new ParkingLot(); // default MAX_TOTAL_AREA = 100 m²
    }

    @Test
    public void testParkVehicleTracksOccupiedArea() throws Exception {
        Car car = new Car("34ABC01", 2);          // 10 m²
        Motorcycle moto = new Motorcycle("34MOTO01", 1); // 5 m²

        lot.parkVehicle(car);
        assertEquals(10, lot.getTotalOccupiedArea());
        assertEquals(90, lot.getAvailableArea());

        lot.parkVehicle(moto);
        assertEquals(15, lot.getTotalOccupiedArea());
        assertEquals(85, lot.getAvailableArea());
    }

    @Test
    public void testDuplicatePlateThrowsException() throws Exception {
        Car car1 = new Car("34ABC01", 2);
        Car car2 = new Car("34ABC01", 5);

        lot.parkVehicle(car1);
        assertThrows(VehicleAlreadyParkedException.class, () -> lot.parkVehicle(car2));
    }

    @Test
    public void testParkingLotFullThrowsException() throws Exception {
        // Create lot with small capacity of 25 m²
        ParkingLot smallLot = new ParkingLot(25);

        smallLot.parkVehicle(new Car("34CAR01", 1)); // occupies 10 m² (remaining: 15 m²)
        smallLot.parkVehicle(new Car("34CAR02", 1)); // occupies 10 m² (remaining: 5 m²)
        smallLot.parkVehicle(new Motorcycle("34MOTO01", 1)); // occupies 5 m² (remaining: 0 m²)

        assertEquals(25, smallLot.getTotalOccupiedArea());
        assertEquals(0, smallLot.getAvailableArea());

        // Attempting to park another car (needs 10 m²) must throw ParkingLotFullException
        assertThrows(ParkingLotFullException.class, () -> smallLot.parkVehicle(new Car("34CAR03", 1)));

        // Attempting to park another motorcycle (needs 5 m²) must also throw ParkingLotFullException
        assertThrows(ParkingLotFullException.class, () -> smallLot.parkVehicle(new Motorcycle("34MOTO02", 1)));
    }

    @Test
    public void testCheckOutFreesOccupiedAreaAndCalculatesFee() throws Exception {
        Car car = new Car("34CAR01", 3);                 // 10 m², 3h -> 3 * 10 * 2 = 60 TL
        Motorcycle moto = new Motorcycle("34MOTO01", 2); // 5 m², 2h -> 2 * 5 * 2 = 20 TL

        lot.parkVehicle(car);
        lot.parkVehicle(moto);
        assertEquals(15, lot.getTotalOccupiedArea());

        double carFee = lot.checkOutVehicle("34CAR01");
        assertEquals(60.0, carFee);
        assertEquals(5, lot.getTotalOccupiedArea());
        assertEquals(95, lot.getAvailableArea());

        double motoFee = lot.checkOutVehicle("34MOTO01");
        assertEquals(20.0, motoFee);
        assertEquals(0, lot.getTotalOccupiedArea());
        assertEquals(100, lot.getAvailableArea());
    }

    @Test
    public void testCheckOutNotFoundThrowsException() {
        assertThrows(VehicleNotFoundException.class, () -> lot.checkOutVehicle("NONEXISTENT"));
    }

    @Test
    public void testSortByPlateComparable() throws Exception {
        lot.parkVehicle(new Car("34ZZZ99", 1));
        lot.parkVehicle(new Car("34AAA01", 1));
        lot.parkVehicle(new Motorcycle("34MMM55", 1));

        lot.sortByPlate();

        List<Vehicle> list = lot.getAllVehicles();
        assertEquals("34AAA01", list.get(0).getPlateNumber());
        assertEquals("34MMM55", list.get(1).getPlateNumber());
        assertEquals("34ZZZ99", list.get(2).getPlateNumber());
    }
}
