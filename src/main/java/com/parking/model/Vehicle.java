package com.parking.model;

/**
 * Abstract base class representing any parked vehicle.
 * Implements Comparable for natural sorting by license plate.
 */
public abstract class Vehicle implements Comparable<Vehicle> {

    // Base unit rate per square meter per hour (in TL / m² / hour)
    public static final double BASE_RATE_PER_SQM = 2.0;

    // Encapsulated private fields
    private String plateNumber;
    private int hoursParked;

    // Constructor establishing a valid object state
    public Vehicle(String plateNumber, int hoursParked) {
        if (plateNumber == null || plateNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate cannot be empty.");
        }
        if (hoursParked < 0) {
            throw new IllegalArgumentException("Parked hours cannot be negative.");
        }
        this.plateNumber = plateNumber.trim().toUpperCase();
        this.hoursParked = hoursParked;
    }

    // Abstract method returning the area (in m²) occupied by the vehicle
    public abstract int getOccupiedArea();

    // Abstract method: overridden by subclasses for runtime polymorphism
    public abstract double calculateFee();

    // Abstract method returning the vehicle type name
    public abstract String getType();

    // Abstract method to format vehicle data for file storage
    public abstract String toFileString();

    public String getPlateNumber() {
        return plateNumber;
    }

    public int getHoursParked() {
        return hoursParked;
    }

    public void setHoursParked(int hoursParked) {
        if (hoursParked < 0) {
            throw new IllegalArgumentException("Parked hours cannot be negative.");
        }
        this.hoursParked = hoursParked;
    }

    // Natural ordering: sorted alphabetically by plate number
    @Override
    public int compareTo(Vehicle other) {
        return this.plateNumber.compareToIgnoreCase(other.plateNumber);
    }

    @Override
    public String toString() {
        return "[" + getType() + "] Plate: " + plateNumber + " | Area: " + getOccupiedArea() + " m² | Hours: " + hoursParked + "h | Fee: " + calculateFee() + " TL";
    }
}
