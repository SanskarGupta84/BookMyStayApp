import java.util.*;
import java.io.*;

public class BookMyStayApp {
  public static void main(String[] args) {
    RoomInventory inventory = new RoomInventory();
    FilePersistenceService persistenceService = new FilePersistenceService();
    String filePath = "inventory.txt";

    inventory.updateAvailability("Single", 5);
    inventory.updateAvailability("Double", 3);
    inventory.updateAvailability("Suite", 2);

    persistenceService.saveInventory(inventory, filePath);
    System.out.println("Inventory saved successfully.");

    RoomInventory restoredInventory = new RoomInventory();
    persistenceService.loadInventory(restoredInventory, filePath);

    System.out.println("Restored Inventory:");
    restoredInventory.getRoomAvailability().forEach((type, count) -> System.out.println(type + ": " + count));
  }
}

class FilePersistenceService {
  public void saveInventory(RoomInventory inventory, String filePath) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
      for (Map.Entry<String, Integer> entry : inventory.getRoomAvailability().entrySet()) {
        writer.println(entry.getKey() + "-" + entry.getValue());
      }
    } catch (IOException e) {
      System.out.println("Error saving inventory: " + e.getMessage());
    }
  }

  public void loadInventory(RoomInventory inventory, String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("-");
        if (parts.length == 2) {
          inventory.updateAvailability(parts[0], Integer.parseInt(parts[1]));
        }
      }
    } catch (IOException e) {
      System.out.println("Starting with fresh state (no persistence file found).");
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
