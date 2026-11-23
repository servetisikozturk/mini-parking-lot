package com.parking.service;

import com.parking.exception.ParkingLotFullException;
import com.parking.exception.VehicleAlreadyParkedException;
import com.parking.exception.VehicleNotFoundException;
import com.parking.model.Vehicle;
import com.parking.storage.VehicleStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service class managing parking lot business operations.
 * Enforces total capacity limits based on occupied area in square meters.
 */
public class ParkingLot {

    // Default maximum total parking area in square meters
    public static final int MAX_TOTAL_AREA = 100;

    private final int maxTotalArea;
    private final List<Vehicle> vehicles;

    public ParkingLot() {
        this(MAX_TOTAL_AREA);
    }

    public ParkingLot(int maxTotalArea) {
        if (maxTotalArea <= 0) {
            throw new IllegalArgumentException("Max total area must be greater than zero.");
        }
        this.maxTotalArea = maxTotalArea;
        this.vehicles = new ArrayList<>();
    }

    /**
     * Calculates the sum of occupied areas across all parked vehicles.
     */
    public int getTotalOccupiedArea() {
        int total = 0;
        for (Vehicle v : vehicles) {
            total += v.getOccupiedArea();
        }
        return total;
    }

    /**
     * Returns the remaining available area in square meters.
     */
    public int getAvailableArea() {
        return maxTotalArea - getTotalOccupiedArea();
    }

    public int getMaxTotalArea() {
        return maxTotalArea;
    }

    /**
     * Parks a vehicle.
     * Throws VehicleAlreadyParkedException if the plate already exists.
     * Throws ParkingLotFullException if the vehicle's area exceeds remaining space.
     */
    public void parkVehicle(Vehicle vehicle) throws VehicleAlreadyParkedException, ParkingLotFullException {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null.");
        }

        // Validate unique plate
        for (Vehicle existing : vehicles) {
            if (existing.getPlateNumber().equalsIgnoreCase(vehicle.getPlateNumber())) {
                throw new VehicleAlreadyParkedException("Vehicle with plate '" + vehicle.getPlateNumber() + "' is already parked!");
            }
        }

        // Validate area capacity
        if (getTotalOccupiedArea() + vehicle.getOccupiedArea() > maxTotalArea) {
            throw new ParkingLotFullException("Cannot park vehicle " + vehicle.getPlateNumber() +
                    ": Parking lot capacity exceeded! (Available: " + getAvailableArea() +
                    " m², Required: " + vehicle.getOccupiedArea() + " m²)");
        }

        vehicles.add(vehicle);
    }

    /**
     * Finds a parked vehicle by its plate number.
     */
    public Vehicle findVehicle(String plateNumber) throws VehicleNotFoundException {
        if (plateNumber == null) {
            throw new VehicleNotFoundException("Plate number cannot be null.");
        }
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getPlateNumber().equalsIgnoreCase(plateNumber.trim())) {
                return vehicle;
            }
        }
        throw new VehicleNotFoundException("Vehicle with plate '" + plateNumber + "' not found in parking lot.");
    }

    /**
     * Checks out a vehicle, removes it from the lot, frees its area, and returns the calculated parking fee.
     * Demonstrates runtime polymorphism via vehicle.calculateFee().
     */
    public double checkOutVehicle(String plateNumber) throws VehicleNotFoundException {
        Vehicle vehicle = findVehicle(plateNumber);
        vehicles.remove(vehicle);
        // Runtime polymorphism: delegates fee calculation to Car or Motorcycle
        return vehicle.calculateFee();
    }

    /**
     * Sorts vehicles alphabetically by license plate using Comparable.
     */
    public void sortByPlate() {
        Collections.sort(vehicles);
    }

    /**
     * Sorts vehicles by parking duration in descending order using a lambda expression.
     */
    public void sortByHoursDesc() {
        vehicles.sort((v1, v2) -> Integer.compare(v2.getHoursParked(), v1.getHoursParked()));
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicles);
    }

    public void load(VehicleStorage storage) throws IOException {
        vehicles.clear();
        List<Vehicle> loaded = storage.loadVehicles();
        for (Vehicle v : loaded) {
            try {
                parkVehicle(v);
            } catch (VehicleAlreadyParkedException | ParkingLotFullException e) {
                // Skip invalid or overflowing records during reload
            }
        }
    }

    public void save(VehicleStorage storage) throws IOException {
        storage.saveVehicles(vehicles);
    }
}
