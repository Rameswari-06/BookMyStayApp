import java.util.*;
import java.util.concurrent.*;

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

// ---------------- Thread-Safe Inventory ----------------
class RoomInventory {
    private final Map<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        } else {
            return false;
        }
    }

    public synchronized void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Available: " + inventory.get(type));
        }
    }
}

// ---------------- Booking Service ----------------
class BookingService {

    private final RoomInventory inventory;
    private int counter = 1;

    BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public synchronized void processBooking(Reservation r) {
        boolean allocated = inventory.allocateRoom(r.roomType);
        if (allocated) {
            String id = r.roomType.replace(" ", "") + "-" + counter++;
            r.setReservationId(id);
            System.out.println("Booking Confirmed by Thread " +
                    Thread.currentThread().getName() + ":");
            r.display();
        } else {
            System.out.println("Booking Failed for " + r.guestName +
                    " (No rooms available for " + r.roomType + ")");
        }
    }
}

// ---------------- Worker Thread ----------------
class BookingWorker implements Runnable {

    private final Queue<Reservation> queue;
    private final BookingService service;

    BookingWorker(Queue<Reservation> queue, BookingService service) {
        this.queue = queue;
        this.service = service;
    }

    @Override
    public void run() {
        while (true) {
            Reservation r;
            synchronized (queue) {
                if (queue.isEmpty()) break;
                r = queue.poll();
            }
            service.processBooking(r);
        }
    }
}

// ---------------- MAIN CLASS ----------------
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Use Case 11 (Concurrent Booking Simulation)\n");

        // Shared inventory
        RoomInventory inventory = new RoomInventory();

        // Shared booking queue
        Queue<Reservation> bookingQueue = new LinkedList<>();

        // Simulate multiple booking requests
        bookingQueue.add(new Reservation("Alice", "Single Room"));
        bookingQueue.add(new Reservation("Bob", "Double Room"));
        bookingQueue.add(new Reservation("Charlie", "Suite Room"));
        bookingQueue.add(new Reservation("David", "Single Room"));
        bookingQueue.add(new Reservation("Eve", "Double Room"));
        bookingQueue.add(new Reservation("Frank", "Single Room"));
        bookingQueue.add(new Reservation("Grace", "Suite Room"));
        bookingQueue.add(new Reservation("Hank", "Single Room"));

        BookingService service = new BookingService(inventory);

        // Create multiple threads to simulate concurrent processing
        int numThreads = 3;
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new BookingWorker(bookingQueue, service), "Thread-" + (i + 1));
            threads[i].start();
        }

        // Wait for all threads to finish
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Show final inventory
        inventory.displayInventory();
    }
}