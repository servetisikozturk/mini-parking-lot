package com.parking.model;

/**
 * Concrete class representing a standard automobile.
 * Demonstrates inheritance and polymorphism.
 */
public class Car extends Vehicle {

    // Hourly parking rate for cars (20.0 TL / hour)
    private static final double HOURLY_RATE = 20.0;

    public Car(String plateNumber, int hoursParked) {
        super(plateNumber, hoursParked);
    }

    // Overridden method implementing fee calculation for cars
    @Override
    public double calculateFee() {
        return getHoursParked() * HOURLY_RATE;
    }

    @Override
    public String getType() {
        return "Car";
    }

    @Override
    public String toFileString() {
        return "CAR;" + getPlateNumber() + ";" + getHoursParked();
    }
}
