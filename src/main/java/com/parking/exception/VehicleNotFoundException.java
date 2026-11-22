package com.parking.exception;

/**
 * Custom checked exception thrown when a vehicle plate cannot be found in the lot.
 */
public class VehicleNotFoundException extends Exception {

    public VehicleNotFoundException(String message) {
        super(message);
    }
}
