# Mini Parking Lot Management System

A simple, robust, console-based vehicle parking management application developed in Java with Maven. The system bases space allocation and hourly pricing on the area (in square meters) each vehicle occupies.

---

## 1. Problem Statement
Managing a vehicle parking facility requires allocating space fairly, preventing over-capacity, and charging vehicles proportionally to the ground area they occupy. This application manages lot occupancy within a 100 m² physical limit, calculates hourly fees dynamically based on occupied square meters, prevents duplicate entries, and persists records across restarts.

---

## 2. Implemented Technical Requirements

### Encapsulation
- All state variables in `Vehicle`, `Car`, and `Motorcycle` are private.
- Constructor validations ensure plate numbers cannot be blank and hours cannot be negative.

### Abstraction
- `Vehicle` is an abstract base class defining core contracts:
  - `public abstract int getOccupiedArea();` (returns occupied area in m²)
  - `public abstract double calculateFee();`
  - `public abstract String getType();`
  - `public abstract String toFileString();`
- Defines the common unit rate constant `BASE_RATE_PER_SQM = 2.0 TL/m²/h`.

### Inheritance and Runtime Polymorphism
- `Car` and `Motorcycle` inherit from `Vehicle`.
- **Occupied Area**:
  - `Car`: Occupies **10 m²**.
  - `Motorcycle`: Occupies **5 m²**.
- **Fee Calculation**:
  - `Car`: `hours * getOccupiedArea() * BASE_RATE_PER_SQM` ($10 \times 2.0 = 20.0$ TL/h).
  - `Motorcycle`: `hours * getOccupiedArea() * BASE_RATE_PER_SQM` ($5 \times 2.0 = 10.0$ TL/h).
- At checkout, calling `vehicle.calculateFee()` dynamically delegates to the subclass implementation.

### Capacity Tracking & Custom Domain Exceptions
- **Total Area Limit**: `ParkingLot` enforces `MAX_TOTAL_AREA = 100 m²`.
- **`ParkingLotFullException`**: Thrown if attempting to park a vehicle whose required area exceeds the lot's available space.
- **`VehicleAlreadyParkedException`**: Thrown if a vehicle with the same license plate is already parked.
- **`VehicleNotFoundException`**: Thrown if attempting to check out a non-existent plate.

### Meaningful Relationships
- `ParkingLot` maintains an **aggregation** relationship with `Vehicle` (`List<Vehicle> vehicles = new ArrayList<>()`), with 1 to 0..* multiplicity.
- Tracks `getTotalOccupiedArea()` and `getAvailableArea()`.

### Collections, Generics & Sorting
- Uses generic collection `List<Vehicle>`.
- Natural sorting: `Vehicle` implements `Comparable<Vehicle>` to sort alphabetically by license plate.
- Alternate sorting: Uses a lambda expression (`Comparator`) to sort by parked duration in descending order.

### Persistence
- `VehicleStorage` reads and writes vehicle records to `data/vehicles.txt`.
- Employs `try-with-resources` (`BufferedReader`, `BufferedWriter`) to guarantee automatic stream closure without leaks.

---

## 3. Design Pattern: Factory Method
- **Pattern Name**: Factory Pattern
- **Participating Classes**:
  - `VehicleFactory`: Creator providing the static factory method `createVehicle(...)`.
  - `Vehicle`: Product abstraction.
  - `Car` and `Motorcycle`: Concrete products.
  - `ParkingApp` and `VehicleStorage`: Clients requesting vehicle creation.
- **Benefit**:
  Decouples client components from concrete constructors (`new Car(...)`, `new Motorcycle(...)`). Adding a new vehicle type (e.g. `Truck` occupying 25 m²) requires only adding the subclass and a case in the factory, leaving UI and storage untouched.

---

## 4. Package Structure
```
src/main/java/com/parking/
  ├── exception/   # VehicleAlreadyParkedException, ParkingLotFullException, VehicleNotFoundException
  ├── factory/     # VehicleFactory
  ├── model/       # Vehicle, Car, Motorcycle
  ├── service/     # ParkingLot
  ├── storage/     # VehicleStorage
  └── ui/          # ParkingApp (Main entry point)
```

---

## 5. Build and Execution Instructions

### Prerequisites
- Java JDK 17 or higher
- Apache Maven 3.8+

### Compile Project
```bash
mvn clean compile
```

### Run Unit Tests
```bash
mvn test
```

### Run Console Application
```bash
mvn exec:java
```

## Known Limitations
- **Console-Only Interface:** The application currently operates via a command-line interface (CLI); a graphical user interface (GUI) is not yet implemented.
- **File-Based Storage:** Vehicle records are persisted in a local flat text file (`data/vehicles.txt`) rather than a relational database, which limits concurrent multi-user access.
- **Static Pricing & Capacity:** Hourly rates (`2.0 TL/m²/h`) and maximum lot area (`100 m²`) are defined as compile-time constants rather than dynamic configuration settings.

## Author
- **Servet Isik Ozturk** (Neptun ID: `ERPV2D`) — BSc Computer Science Engineering, University of Pécs (MIK)
### Package into Executable JAR
```bash
mvn package
java -jar target/mini-parking-lot-1.0.0.jar
```
