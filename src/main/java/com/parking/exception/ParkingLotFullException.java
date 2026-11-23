package com.parking.exception;

/**
 * Custom checked exception thrown when a vehicle cannot be parked
 * because the total available parking area would be exceeded.
 */
public class ParkingLotFullException extends Exception {

    public ParkingLotFullException(String message) {
        super(message);
    }
}
