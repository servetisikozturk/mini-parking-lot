package com.parking.storage;

import com.parking.factory.VehicleFactory;
import com.parking.model.Vehicle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading vehicles to/from a text file using try-with-resources.
 */
public class VehicleStorage {

    private final String filePath;

    public VehicleStorage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads vehicles from the text file.
     * Uses try-with-resources to automatically close the file reader.
     */
    public List<Vehicle> loadVehicles() throws IOException {
        List<Vehicle> list = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // File format: TYPE;PLATE;HOURS
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    String type = parts[0];
                    String plate = parts[1];
                    int hours = Integer.parseInt(parts[2].trim());

                    // Use the Factory to create objects
                    Vehicle vehicle = VehicleFactory.createVehicle(type, plate, hours);
                    list.add(vehicle);
                }
            }
        }
        return list;
    }

    /**
     * Writes all vehicles to the text file.
     * Uses try-with-resources to automatically flush and close the file writer.
     */
    public void saveVehicles(List<Vehicle> vehicles) throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Vehicle vehicle : vehicles) {
                writer.write(vehicle.toFileString());
                writer.newLine();
            }
        }
    }
}
