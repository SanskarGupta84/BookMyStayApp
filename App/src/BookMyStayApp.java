import java.util.HashMap;
import java.util.Map;

public class BookMyStayApp {
  public static void main(String[] args) {
    RoomInventory inventory = new RoomInventory();

    inventory.updateAvailability("Single", 5);
    inventory.updateAvailability("Double", 3);
    inventory.updateAvailability("Suite", 2);

    Map<String, Integer> currentInventory = inventory.getRoomAvailability();

    for (String type : currentInventory.keySet()) {
      Room room;
      if (type.equals("Single"))
        room = new SingleRoom();
      else if (type.equals("Double"))
        room = new DoubleRoom();
      else
        room = new SuiteRoom();

      System.out.println(type + " Room:");
      room.displayDetails();
      System.out.println("Available Rooms: " + currentInventory.get(type) + "\n");
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
    super(1, 200, 1500.0);
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
