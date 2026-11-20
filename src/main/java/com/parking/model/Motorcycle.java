package com.parking.model;

/**
 * Concrete class representing a motorcycle.
 * Demonstrates inheritance and polymorphism.
 */
public class Motorcycle extends Vehicle {

    // Hourly parking rate for motorcycles (10.0 TL / hour)
    private static final double HOURLY_RATE = 10.0;

    public Motorcycle(String plateNumber, int hoursParked) {
        super(plateNumber, hoursParked);
    }

    // Overridden method implementing fee calculation for motorcycles
    @Override
    public double calculateFee() {
        return getHoursParked() * HOURLY_RATE;
    }

    @Override
    public String getType() {
        return "Motorcycle";
    }

    @Override
    public String toFileString() {
        return "MOTORCYCLE;" + getPlateNumber() + ";" + getHoursParked();
    }
}
