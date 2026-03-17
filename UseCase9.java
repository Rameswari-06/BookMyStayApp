import java.util.*;

// ---------------- Custom Exception ----------------
class InvalidBookingException extends Exception {
    InvalidBookingException(String message) {
        super(message);
    }
}

// ---------------- Reservation ----------------
class Reservation {
    String guestName;
    String roomType;
    String reservationId;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void setReservationId(String id) {
        this.reservationId = id;
    }

    void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// ---------------- Inventory ----------------
class RoomInventory {
    HashMap<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }

    int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    void decreaseAvailability(String type) throws InvalidBookingException {
        int available = getAvailability(type);
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for " + type);
        }
        inventory.put(type, available - 1);
    }

    void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Available: " + inventory.get(type));
        }
    }
}

// ---------------- Validator ----------------
class BookingValidator {

    RoomInventory inventory;

    BookingValidator(RoomInventory inventory) {
        this.inventory = inventory;
    }

    void validate(Reservation r) throws InvalidBookingException {
        if (r.guestName == null || r.guestName.isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (!inventory.isValidRoomType(r.roomType)) {
            throw new InvalidBookingException("Invalid room type: " + r.roomType);
        }

        if (inventory.getAvailability(r.roomType) <= 0) {
            throw new InvalidBookingException("No availability for room type: " + r.roomType);
        }
    }
}

// ---------------- Booking Service ----------------
class BookingService {

    int counter = 1;
    RoomInventory inventory;
    BookingValidator validator;

    BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.validator = new BookingValidator(inventory);
    }

    void processBooking(Reservation r) {
        try {
            // Validate before processing
            validator.validate(r);

            // Assign reservation ID
            String id = r.roomType.replace(" ", "") + "-" + counter++;
            r.setReservationId(id);

            // Update inventory
            inventory.decreaseAvailability(r.roomType);

            System.out.println("Booking Confirmed!");
            r.display();
            System.out.println();

        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage() + "\n");
        }
    }
}

// ---------------- MAIN CLASS ----------------
public class UseCase9 {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Use Case 9 (Error Handling & Validation)\n");

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Test booking requests (some invalid)
        List<Reservation> requests = Arrays.asList(
                new Reservation("Alice", "Single Room"),
                new Reservation("", "Double Room"),          // Invalid guest name
                new Reservation("Charlie", "Suite Room"),
                new Reservation("David", "Penthouse"),       // Invalid room type
                new Reservation("Eve", "Single Room"),
                new Reservation("Frank", "Single Room"),
                new Reservation("Grace", "Single Room"),
                new Reservation("Hank", "Single Room")       // Exceeding availability
        );

        for (Reservation r : requests) {
            bookingService.processBooking(r);
        }

        // Show remaining inventory
        inventory.displayInventory();
    }
}