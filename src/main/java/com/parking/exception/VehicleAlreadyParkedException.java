package com.parking.exception;

/**
 * Custom checked exception thrown when a vehicle with the same plate
 * is already parked in the lot.
 */
public class VehicleAlreadyParkedException extends Exception {

    public VehicleAlreadyParkedException(String message) {
        super(message);
    }
}
