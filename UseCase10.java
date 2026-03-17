import java.util.*;

// ---------------- Reservation ----------------
class Reservation {
    String guestName;
    String roomType;
    String reservationId;
    boolean cancelled;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.cancelled = false;
    }

    void setReservationId(String id) {
        this.reservationId = id;
    }

    void cancel() {
        this.cancelled = true;
    }

    void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType +
                " | Status: " + (cancelled ? "Cancelled" : "Confirmed"));
    }
}

// ---------------- Booking History ----------------
class BookingHistory {

    List<Reservation> history = new ArrayList<>();

    void addReservation(Reservation r) {
        history.add(r);
    }

    boolean exists(String reservationId) {
        for (Reservation r : history) {
            if (r.reservationId.equals(reservationId)) {
                return true;
            }
        }
        return false;
    }

    Reservation getReservationById(String reservationId) {
        for (Reservation r : history) {
            if (r.reservationId.equals(reservationId)) {
                return r;
            }
        }
        return null;
    }

    void displayAll() {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : history) {
            r.display();
        }
    }
}

// ---------------- Room Inventory ----------------
class RoomInventory {
    HashMap<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    void decreaseAvailability(String type) {
        inventory.put(type, getAvailability(type) - 1);
    }

    void increaseAvailability(String type) {
        inventory.put(type, getAvailability(type) + 1);
    }

    void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Available: " + inventory.get(type));
        }
    }
}

// ---------------- Booking Service ----------------
class BookingService {

    int counter = 1;
    RoomInventory inventory;
    BookingHistory history;
    Stack<String> releasedRoomIds = new Stack<>();

    BookingService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    void confirmBooking(Reservation r) {
        String id = r.roomType.replace(" ", "") + "-" + counter++;
        r.setReservationId(id);
        inventory.decreaseAvailability(r.roomType);
        history.addReservation(r);
        System.out.println("Booking Confirmed!");
        r.display();
    }

    void cancelBooking(String reservationId) {
        Reservation r = history.getReservationById(reservationId);

        if (r == null) {
            System.out.println("Cancellation Failed: Reservation ID not found.");
            return;
        }

        if (r.cancelled) {
            System.out.println("Cancellation Failed: Reservation already cancelled.");
            return;
        }

        // Perform rollback
        r.cancel();
        inventory.increaseAvailability(r.roomType);
        releasedRoomIds.push(r.reservationId);

        System.out.println("Booking Cancelled Successfully!");
        r.display();
    }

    void displayReleasedRoomIds() {
        System.out.println("\nReleased Room IDs (LIFO Stack): " + releasedRoomIds);
    }
}

// ---------------- MAIN CLASS ----------------
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Use Case 10 (Booking Cancellation & Rollback)\n");

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        BookingService service = new BookingService(inventory, history);

        // Confirm some bookings
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");

        service.confirmBooking(r1);
        service.confirmBooking(r2);
        service.confirmBooking(r3);

        // Attempt cancellations
        service.cancelBooking("SingleRoom-1");    // Valid cancellation
        service.cancelBooking("DoubleRoom-1");    // Valid cancellation
        service.cancelBooking("DoubleRoom-1");    // Already cancelled
        service.cancelBooking("NonExist-99");     // Non-existent

        // Show booking history
        history.displayAll();

        // Show released room IDs stack
        service.displayReleasedRoomIds();

        // Show current inventory
        inventory.displayInventory();
    }
}