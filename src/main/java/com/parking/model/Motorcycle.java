package com.parking.model;

/**
 * Concrete class representing a motorcycle.
 * Demonstrates inheritance and polymorphism.
 */
public class Motorcycle extends Vehicle {

    // Motorcycle occupies 5 m² of parking area
    private static final int OCCUPIED_AREA = 5;

    public Motorcycle(String plateNumber, int hoursParked) {
        super(plateNumber, hoursParked);
    }

    @Override
    public int getOccupiedArea() {
        return OCCUPIED_AREA;
    }

    // Hourly fee based on occupied area: hours * area (5 m²) * base rate (2.0 TL/m²/h) = 10.0 TL/h
    @Override
    public double calculateFee() {
        return getHoursParked() * getOccupiedArea() * BASE_RATE_PER_SQM;
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
