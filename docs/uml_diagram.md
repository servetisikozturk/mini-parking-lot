# UML Class Diagram & Architecture

This document describes the Object-Oriented design and structure of the Mini Parking Lot application, including area-based capacity and pricing.

## Mermaid Class Diagram

```mermaid
classDiagram
    class Comparable~Vehicle~ {
        <<interface>>
        +compareTo(other: Vehicle) int
    }

    class Vehicle {
        <<abstract>>
        +BASE_RATE_PER_SQM: double = 2.0$
        -plateNumber: String
        -hoursParked: int
        +Vehicle(plateNumber: String, hoursParked: int)
        +getOccupiedArea()* int
        +calculateFee()* double
        +getType()* String
        +toFileString()* String
        +getPlateNumber() String
        +getHoursParked() int
        +setHoursParked(hoursParked: int) void
        +compareTo(other: Vehicle) int
        +toString() String
    }

    class Car {
        -OCCUPIED_AREA: int = 10$
        +Car(plateNumber: String, hoursParked: int)
        +getOccupiedArea() int
        +calculateFee() double
        +getType() String
        +toFileString() String
    }

    class Motorcycle {
        -OCCUPIED_AREA: int = 5$
        +Motorcycle(plateNumber: String, hoursParked: int)
        +getOccupiedArea() int
        +calculateFee() double
        +getType() String
        +toFileString() String
    }

    class VehicleFactory {
        +createVehicle(type: String, plateNumber: String, hoursParked: int)$ Vehicle
    }

    class VehicleStorage {
        -filePath: String
        +VehicleStorage(filePath: String)
        +loadVehicles() List~Vehicle~
        +saveVehicles(vehicles: List~Vehicle~) void
    }

    class ParkingLot {
        +MAX_TOTAL_AREA: int = 100$
        -maxTotalArea: int
        -vehicles: List~Vehicle~
        +ParkingLot()
        +ParkingLot(maxTotalArea: int)
        +getTotalOccupiedArea() int
        +getAvailableArea() int
        +getMaxTotalArea() int
        +parkVehicle(vehicle: Vehicle) void
        +findVehicle(plateNumber: String) Vehicle
        +checkOutVehicle(plateNumber: String) double
        +sortByPlate() void
        +sortByHoursDesc() void
        +getAllVehicles() List~Vehicle~
        +load(storage: VehicleStorage) void
        +save(storage: VehicleStorage) void
    }

    class ParkingApp {
        -DATA_FILE: String = "data/vehicles.txt"
        -parkingLot: ParkingLot
        -storage: VehicleStorage
        -scanner: Scanner
        +main(args: String[]) void
        +start() void
    }

    class VehicleAlreadyParkedException {
        +VehicleAlreadyParkedException(message: String)
    }

    class VehicleNotFoundException {
        +VehicleNotFoundException(message: String)
    }

    class ParkingLotFullException {
        +ParkingLotFullException(message: String)
    }

    Comparable <|.. Vehicle : realizes
    Vehicle <|-- Car : extends
    Vehicle <|-- Motorcycle : extends
    VehicleFactory ..> Vehicle : creates
    ParkingLot o-- Vehicle : aggregates (1 to 0..*)
    ParkingLot --> VehicleStorage : uses
    ParkingApp --> ParkingLot : delegates
    ParkingLot ..> VehicleAlreadyParkedException : throws
    ParkingLot ..> VehicleNotFoundException : throws
    ParkingLot ..> ParkingLotFullException : throws
```

## Structure Overview
1. **Inheritance & Polymorphism**: `Car` and `Motorcycle` extend abstract `Vehicle`, providing concrete occupied area definitions (`10 m²` and `5 m²`) and area-based fee calculations (`hours * area * BASE_RATE_PER_SQM`).
2. **Capacity Management**: `ParkingLot` tracks total occupied area against `MAX_TOTAL_AREA` (100 m²), throwing `ParkingLotFullException` when capacity is exceeded.
3. **Factory Pattern**: `VehicleFactory` encapsulates instantiation behind `createVehicle(...)`.
4. **Data Persistence**: `VehicleStorage` handles text persistence using `try-with-resources`.
