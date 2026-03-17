import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;



class Reservation {

    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void display() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}
class BookingRequestQueue {

    Queue<Reservation> queue;

    BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    void addRequest(Reservation r) {
        queue.add(r);
        System.out.println("Booking request added for " + r.guestName);
    }
    boolean hasRequests() {
        return !queue.isEmpty();
    }

    Reservation getNextRequest() {
        return queue.poll();
    }

    void showRequests() {
        System.out.println("\nBooking Requests in Queue:");

        for (Reservation r : queue) {
            r.display();
        }
    }
}

abstract class Room {

    String type;
    int beds;
    double price;

    Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Price: ₹" + price);
    }
}
class SingleRoom extends Room {

    SingleRoom() {
        super("Single Room", 1, 2000);
    }
}
class DoubleRoom extends Room {

    DoubleRoom() {
        super("Double Room", 2, 3500);
    }
}
class SuiteRoom extends Room {

    SuiteRoom() {
        super("Suite Room", 3, 6000);
    }
}
class RoomInventory {

    private HashMap<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
    void decreaseAvailability(String roomType) {
        int current = getAvailability(roomType);
        inventory.put(roomType, current - 1);
    }

    void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    void displayInventory() {
        for (String room : inventory.keySet()) {
            System.out.println(room + " Available: " + inventory.get(room));
        }
    }

    class SearchService {

        void searchAvailableRooms(RoomInventory inventory) {

            SingleRoom single = new SingleRoom();
            DoubleRoom doubleRoom = new DoubleRoom();
            SuiteRoom suite = new SuiteRoom();

            int singleAvailable = inventory.getAvailability("Single Room");
            int doubleAvailable = inventory.getAvailability("Double Room");
            int suiteAvailable = inventory.getAvailability("Suite Room");

            System.out.println("Available Rooms");

            if (singleAvailable > 0) {
                single.displayDetails();
                System.out.println("Available: " + singleAvailable);
                System.out.println();
            }

            if (doubleAvailable > 0) {
                doubleRoom.displayDetails();
                System.out.println("Available: " + doubleAvailable);
                System.out.println();
            }

            if (suiteAvailable > 0) {
                suite.displayDetails();
                System.out.println("Available: " + suiteAvailable);
                System.out.println();
            }
        }

    }
}
class AddOnService {

    String serviceName;
    double price;

    AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    void display() {
        System.out.println(serviceName + " - ₹" + price);
    }
}
class AddOnServiceManager {

    // ReservationID -> List of Services
    HashMap<String, List<AddOnService>> serviceMap = new HashMap<>();

    void addService(String reservationId, AddOnService service) {

        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Service added: " + service.serviceName +
                " for Reservation ID: " + reservationId);
    }

    void showServices(String reservationId) {

        System.out.println("\nServices for Reservation ID: " + reservationId);

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services added.");
            return;
        }

        for (AddOnService s : services) {
            s.display();
        }
    }

    double calculateTotalCost(String reservationId) {

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null) return 0;

        double total = 0;

        for (AddOnService s : services) {
            total += s.price;
        }

        return total;
    }
}
class BookingService {

    HashMap<String, Set<String>> allocatedRooms = new HashMap<>();
    Set<String> usedRoomIds = new HashSet<>();

    String generateRoomId(String roomType, int number) {
        return roomType.replace(" ", "") + "-" + number;
    }

    void processBookings(BookingRequestQueue queue, RoomInventory inventory) {

        int counter = 1;

        while (queue.hasRequests()) {

            Reservation r = queue.getNextRequest();
            String roomType = r.roomType;

            int available = inventory.getAvailability(roomType);

            if (available > 0) {

                String roomId = generateRoomId(roomType, counter++);

                if (!usedRoomIds.contains(roomId)) {

                    usedRoomIds.add(roomId);

                    allocatedRooms.putIfAbsent(roomType, new HashSet<>());
                    allocatedRooms.get(roomType).add(roomId);

                    inventory.decreaseAvailability(roomType);

                    System.out.println("Reservation Confirmed!");
                    System.out.println("Guest: " + r.guestName);
                    System.out.println("Room Type: " + roomType);
                    System.out.println("Assigned Room ID: " + roomId);
                    System.out.println();
                }

            } else {
                System.out.println("No rooms available for " + roomType + " for guest " + r.guestName);
            }
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {
        useCase1();
        useCase2();
        useCase3();
        useCase4();
        useCase5();
        useCase6();
        useCase7();

    }

    public static void useCase1() {
        String appName = "Book My Stay";
        String version = "v1.0";

        System.out.println("=================================");
        System.out.println("Welcome to " + appName);
        System.out.println("Hotel Booking Management System");
        System.out.println("Version: " + version);
        System.out.println("=================================");
    }


    public static void useCase2() {

        SingleRoom single = new SingleRoom();
        DoubleRoom doubleRoom = new DoubleRoom();
        SuiteRoom suite = new SuiteRoom();

        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        System.out.println("Book My Stay - Hotel Booking System v2.0");

        single.displayDetails();
        System.out.println("Available: " + singleAvailable);
        System.out.println();

        doubleRoom.displayDetails();
        System.out.println("Available: " + doubleAvailable);
        System.out.println();

        suite.displayDetails();
        System.out.println("Available: " + suiteAvailable);
    }

    public static void useCase3() {

        System.out.println("Book My Stay - Hotel Booking System v3.0");
        System.out.println();

        RoomInventory inventory = new RoomInventory();
        inventory.displayInventory();
    }

    public static void useCase4() {

        System.out.println("Book My Stay - Hotel Booking System v4.0");
        System.out.println();

        RoomInventory inventory = new RoomInventory();

        RoomInventory.SearchService search = inventory.new SearchService();
        search.searchAvailableRooms(inventory);
    }
    public static void useCase5() {

        System.out.println("Book My Stay - Hotel Booking System v5.0");
        System.out.println();

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");

        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        bookingQueue.showRequests();
    }
    public static void useCase6() {

        System.out.println("\nBook My Stay - Hotel Booking System v6.0\n");

        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));

        BookingService bookingService = new BookingService();

        bookingService.processBookings(bookingQueue, inventory);

        System.out.println("Updated Inventory:\n");
        inventory.displayInventory();
    }
    public static void useCase7() {

        System.out.println("\nBook My Stay - Hotel Booking System v7.0\n");

        // Step 1: Setup booking (same as UC6)
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));

        BookingService bookingService = new BookingService();
        bookingService.processBookings(bookingQueue, inventory);

        // Assume generated ID (based on your logic)
        String reservationId = "SingleRoom-1";

        // Step 2: Add-on services
        AddOnServiceManager manager = new AddOnServiceManager();

        AddOnService wifi = new AddOnService("WiFi", 500);
        AddOnService breakfast = new AddOnService("Breakfast", 800);
        AddOnService spa = new AddOnService("Spa", 1500);

        manager.addService(reservationId, wifi);
        manager.addService(reservationId, breakfast);
        manager.addService(reservationId, spa);

        // Step 3: Show services
        manager.showServices(reservationId);

        // Step 4: Calculate cost
        double total = manager.calculateTotalCost(reservationId);

        System.out.println("\nTotal Add-On Cost: ₹" + total);

        // Verify inventory unchanged
        System.out.println("\nInventory remains unchanged:");
        inventory.displayInventory();
    }
}