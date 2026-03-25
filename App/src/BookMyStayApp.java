import java.util.HashMap;
import java.util.Map;

public class BookMyStayApp {
  public static void main(String[] args) {
    RoomInventory inventory = new RoomInventory();
    inventory.updateAvailability("Single", 5);
    inventory.updateAvailability("Double", 3);
    inventory.updateAvailability("Suite", 0);

    Room single = new SingleRoom();
    Room doubleRm = new DoubleRoom();
    Room suite = new SuiteRoom();

    SearchService searchService = new SearchService();

    System.out.println("--- Room Search Results ---");
    searchService.searchAvailableRooms(inventory, single, doubleRm, suite);
  }
}

class SearchService {
  public void searchAvailableRooms(RoomInventory inventory, Room single, Room doubleRm, Room suite) {
    Map<String, Integer> availability = inventory.getRoomAvailability();

    if (availability.getOrDefault("Single", 0) > 0) {
      System.out.println("Single Room:");
      single.displayDetails();
      System.out.println("Available: " + availability.get("Single") + "\n");
    }

    if (availability.getOrDefault("Double", 0) > 0) {
      System.out.println("Double Room:");
      doubleRm.displayDetails();
      System.out.println("Available: " + availability.get("Double") + "\n");
    }

    if (availability.getOrDefault("Suite", 0) > 0) {
      System.out.println("Suite Room:");
      suite.displayDetails();
      System.out.println("Available: " + availability.get("Suite") + "\n");
    }
  }
}

class RoomInventory {
  private Map<String, Integer> roomAvailability = new HashMap<>();

  public void updateAvailability(String roomType, int count) {
    roomAvailability.put(roomType, count);
  }

  public Map<String, Integer> getRoomAvailability() {
    return roomAvailability;
  }
}

abstract class Room {
  protected int beds;
  protected int size;
  protected double price;

  public Room(int beds, int size, double price) {
    this.beds = beds;
    this.size = size;
    this.price = price;
  }

  public void displayDetails() {
    System.out.println("Beds: " + beds);
    System.out.println("Size: " + size + " sqft");
    System.out.println("Price per night: " + price);
  }
}

class SingleRoom extends Room {
  public SingleRoom() {
    super(1, 250, 1500.0);
  }
}

class DoubleRoom extends Room {
  public DoubleRoom() {
    super(2, 400, 2500.0);
  }
}

class SuiteRoom extends Room {
  public SuiteRoom() {
    super(3, 750, 5000.0);
  }
}
