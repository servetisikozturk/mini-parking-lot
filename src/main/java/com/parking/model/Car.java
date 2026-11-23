package com.parking.model;

/**
 * Concrete class representing a standard automobile.
 * Demonstrates inheritance and polymorphism.
 */
public class Car extends Vehicle {

    // Car occupies 10 m² of parking area
    private static final int OCCUPIED_AREA = 10;

    public Car(String plateNumber, int hoursParked) {
        super(plateNumber, hoursParked);
    }

    @Override
    public int getOccupiedArea() {
        return OCCUPIED_AREA;
    }

    // Hourly fee based on occupied area: hours * area (10 m²) * base rate (2.0 TL/m²/h) = 20.0 TL/h
    @Override
    public double calculateFee() {
        return getHoursParked() * getOccupiedArea() * BASE_RATE_PER_SQM;
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
