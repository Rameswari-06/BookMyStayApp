import java.io.*;
import java.util.*;

// ---------------- Reservation ----------------
class Reservation implements Serializable {
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
class BookingHistory implements Serializable {
    List<Reservation> history = new ArrayList<>();

    void addReservation(Reservation r) {
        history.add(r);
    }

    Reservation getReservationById(String reservationId) {
        for (Reservation r : history) {
            if (r.reservationId.equals(reservationId)) return r;
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
class RoomInventory implements Serializable {
    HashMap<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    void decreaseAvailability(String type) {
        inventory.put(type, getAvailability(type) - 1);
    }

    void increaseAvailability(String type) {
        inventory.put(type, getAvailability(type) + 1);
    }

    int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Available: " + inventory.get(type));
        }
    }
}

// ---------------- Persistence Service ----------------
class PersistenceService {

    private static final String FILE_NAME = "bookmystay_state.ser";

    static void saveState(RoomInventory inventory, BookingHistory history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("\nSystem state saved to file: " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    static Object[] loadState() {
        File f = new File(FILE_NAME);
        if (!f.exists()) {
            System.out.println("No previous state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            RoomInventory inventory = (RoomInventory) ois.readObject();
            BookingHistory history = (BookingHistory) ois.readObject();
            System.out.println("System state loaded from file: " + FILE_NAME);
            return new Object[]{inventory, history};
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state: " + e.getMessage());
            return null;
        }
    }
}

// ---------------- Booking Service ----------------
class BookingService {

    int counter = 1;
    RoomInventory inventory;
    BookingHistory history;

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
        r.cancel();
        inventory.increaseAvailability(r.roomType);
        System.out.println("Booking Cancelled Successfully!");
        r.display();
    }
}

// ---------------- MAIN CLASS ----------------
public class UseCase12 {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Use Case 12 (Data Persistence & System Recovery)");

        Object[] state = PersistenceService.loadState();

        RoomInventory inventory;
        BookingHistory history;

        if (state != null) {
            inventory = (RoomInventory) state[0];
            history = (BookingHistory) state[1];
        } else {
            inventory = new RoomInventory();
            history = new BookingHistory();
        }

        BookingService service = new BookingService(inventory, history);

        // Simulate some bookings
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");

        service.confirmBooking(r1);
        service.confirmBooking(r2);

        // Simulate a cancellation
        service.cancelBooking("SingleRoom-1");

        // Display current state
        inventory.displayInventory();
        history.displayAll();

        // Save state for next run
        PersistenceService.saveState(inventory, history);
    }
}