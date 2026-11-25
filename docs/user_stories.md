# User Stories - Mini Parking Lot Management System

### User Story 1: Park Vehicle and Space Allocation
* **As a** parking lot attendant,
* **I want to** register a vehicle (Car: 10 m² or Motorcycle: 5 m²) with its license plate and parked duration,
* **So that** space is allocated and tracked within the lot's total capacity (100 m²).
* **Acceptance Criteria**:
  1. If the vehicle plate is already parked, the system rejects it by throwing `VehicleAlreadyParkedException`.
  2. If the vehicle's occupied area exceeds the remaining available parking lot area, the system rejects it by throwing `ParkingLotFullException`.

### User Story 2: Check Out and Area-Based Fee Calculation
* **As a** parking lot attendant,
* **I want to** check out a vehicle by its plate number,
* **So that** the system automatically calculates the correct parking fee based on occupied area (`hours * area * BASE_RATE_PER_SQM`), removes the vehicle, and restores the available parking area.
* **Pricing Formula**: `hours * occupiedArea * 2.0 TL/m²/h` (Car: 20.0 TL/h, Motorcycle: 10.0 TL/h).

### User Story 3: View Lot Occupancy and Sort Parked Vehicles
* **As a** manager,
* **I want to** view total occupied and available parking area alongside all parked vehicles, and sort them by plate number or parked duration,
* **So that** I can easily inspect lot occupancy and capacity utilization.

### User Story 4: Data Persistence
* **As a** system operator,
* **I want** parked vehicle data to be loaded on startup and saved to disk on exit,
* **So that** vehicle records and lot state are preserved across application restarts.
