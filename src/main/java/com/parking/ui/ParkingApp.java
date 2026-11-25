package com.parking.ui;

import com.parking.exception.ParkingLotFullException;
import com.parking.exception.VehicleAlreadyParkedException;
import com.parking.exception.VehicleNotFoundException;
import com.parking.factory.VehicleFactory;
import com.parking.model.Vehicle;
import com.parking.service.ParkingLot;
import com.parking.storage.VehicleStorage;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Console user interface for managing the parking lot.
 */
public class ParkingApp {

    private static final String DATA_FILE = "data/vehicles.txt";
    private final ParkingLot parkingLot;
    private final VehicleStorage storage;
    private final Scanner scanner;

    public ParkingApp() {
        this.parkingLot = new ParkingLot();
        this.storage = new VehicleStorage(DATA_FILE);
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        ParkingApp app = new ParkingApp();
        app.start();
    }

    public void start() {
        System.out.println("==============================================");
        System.out.println("  MINI PARKING LOT MANAGEMENT SYSTEM (OOP)    ");
        System.out.println("==============================================");

        // Auto-load data at startup
        try {
            parkingLot.load(storage);
            System.out.println("Loaded " + parkingLot.getAllVehicles().size() +
                    " vehicle(s) from " + DATA_FILE +
                    " (Occupied Area: " + parkingLot.getTotalOccupiedArea() +
                    " m² / " + parkingLot.getMaxTotalArea() + " m²)");
        } catch (IOException e) {
            System.out.println("No existing records found. Starting with an empty lot.");
        }

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Select an option (0-5): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleParkVehicle();
                    break;
                case "2":
                    handleCheckOutVehicle();
                    break;
                case "3":
                    handleListVehicles();
                    break;
                case "4":
                    handleSortVehicles();
                    break;
                case "5":
                    handleSave();
                    break;
                case "0":
                    handleSave();
                    System.out.println("Goodbye! Have a great day.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select 0 to 5.");
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Park New Vehicle (Car / Motorcycle)");
        System.out.println("2. Check Out Vehicle (Calculate & Pay Fee)");
        System.out.println("3. List All Parked Vehicles & Area Status");
        System.out.println("4. Sort Vehicles");
        System.out.println("5. Save to File");
        System.out.println("0. Exit");
    }

    private void handleParkVehicle() {
        System.out.println("\n--- Park Vehicle ---");
        System.out.print("Enter vehicle type (CAR / MOTORCYCLE): ");
        String type = scanner.nextLine().trim();

        System.out.print("Enter license plate (e.g. 34ABC01): ");
        String plate = scanner.nextLine().trim();

        System.out.print("Enter parked hours: ");
        String hoursStr = scanner.nextLine().trim();

        int hours;
        try {
            hours = Integer.parseInt(hoursStr);
        } catch (NumberFormatException e) {
            System.out.println("Error: Hours must be a valid number.");
            return;
        }

        try {
            // Instantiate using Factory Pattern
            Vehicle vehicle = VehicleFactory.createVehicle(type, plate, hours);
            parkingLot.parkVehicle(vehicle);
            System.out.println("Success! Vehicle parked: " + vehicle);
            System.out.println("Lot Area Status: " + parkingLot.getTotalOccupiedArea() +
                    " m² used / " + parkingLot.getAvailableArea() + " m² available.");
        } catch (IllegalArgumentException e) {
            System.out.println("Input Error: " + e.getMessage());
        } catch (VehicleAlreadyParkedException e) {
            System.out.println("Domain Rule Violation: " + e.getMessage());
        } catch (ParkingLotFullException e) {
            System.out.println("Capacity Limit Reached: " + e.getMessage());
        }
    }

    private void handleCheckOutVehicle() {
        System.out.println("\n--- Check Out Vehicle ---");
        System.out.print("Enter license plate to check out: ");
        String plate = scanner.nextLine().trim();

        try {
            double fee = parkingLot.checkOutVehicle(plate);
            System.out.printf("Vehicle checked out successfully. Total Parking Fee: %.2f TL%n", fee);
            System.out.println("Freed up space. Available Area: " + parkingLot.getAvailableArea() + " m².");
        } catch (VehicleNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleListVehicles() {
        List<Vehicle> list = parkingLot.getAllVehicles();
        System.out.println("\n--- PARKING LOT STATUS ---");
        System.out.println("Capacity: " + parkingLot.getTotalOccupiedArea() + " m² / " +
                parkingLot.getMaxTotalArea() + " m² used (Remaining: " +
                parkingLot.getAvailableArea() + " m²)");

        if (list.isEmpty()) {
            System.out.println("The parking lot is currently empty.");
            return;
        }

        System.out.println("\nCurrently Parked Vehicles (" + list.size() + "):");
        for (Vehicle v : list) {
            System.out.println(" - " + v);
        }
    }

    private void handleSortVehicles() {
        List<Vehicle> list = parkingLot.getAllVehicles();
        if (list.isEmpty()) {
            System.out.println("Parking lot is empty, nothing to sort.");
            return;
        }

        System.out.println("\n--- Sort Options ---");
        System.out.println("1. Sort by Plate Number (Comparable)");
        System.out.println("2. Sort by Parked Hours (Descending, Lambda)");
        System.out.print("Choose (1 or 2): ");
        String sortChoice = scanner.nextLine().trim();

        if (sortChoice.equals("1")) {
            parkingLot.sortByPlate();
            System.out.println("Vehicles sorted by Plate Number:");
        } else if (sortChoice.equals("2")) {
            parkingLot.sortByHoursDesc();
            System.out.println("Vehicles sorted by Hours Parked (Highest first):");
        } else {
            System.out.println("Unrecognized choice. Displaying current list:");
        }

        for (Vehicle v : parkingLot.getAllVehicles()) {
            System.out.println(" - " + v);
        }
    }

    private void handleSave() {
        try {
            parkingLot.save(storage);
            System.out.println("Parking lot data successfully saved to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Failed to save data: " + e.getMessage());
        }
    }
}
