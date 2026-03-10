import java.util.HashMap;
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

    void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    void displayInventory() {
        for (String room : inventory.keySet()) {
            System.out.println(room + " Available: " + inventory.get(room));
        }
    }
}
public class BookMyStayApp {

    public static void main(String[] args) {
        useCase1();
        useCase2();
        useCase3();
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
}