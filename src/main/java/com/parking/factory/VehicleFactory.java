package com.parking.factory;

import com.parking.model.Car;
import com.parking.model.Motorcycle;
import com.parking.model.Vehicle;

/**
 * Factory class implementing the Factory Pattern.
 * Centralizes the creation of Vehicle objects behind a common method.
 */
public class VehicleFactory {

    /**
     * Creates and returns a concrete Vehicle instance based on type.
     *
     * @param type        "CAR" or "MOTORCYCLE" (case-insensitive)
     * @param plateNumber Vehicle license plate
     * @param hoursParked Duration in hours
     * @return Concrete Vehicle instance
     */
    public static Vehicle createVehicle(String type, String plateNumber, int hoursParked) {
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null.");
        }

        String normalized = type.trim().toUpperCase();
        switch (normalized) {
            case "CAR":
                return new Car(plateNumber, hoursParked);
            case "MOTORCYCLE":
                return new Motorcycle(plateNumber, hoursParked);
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + type + ". Must be CAR or MOTORCYCLE.");
        }
    }
}
