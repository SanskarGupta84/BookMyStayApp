import java.util.*;

public class BookMyStayApp {
  public static void main(String[] args) {
    RoomInventory inventory = new RoomInventory();
    CancellationService cancellationService = new CancellationService();

    inventory.updateAvailability("Single", 5);

    String resId = "Single-101";
    cancellationService.registerBooking(resId, "Single");

    cancellationService.cancelBooking(resId, inventory);

    System.out.println("Updated Single Room Availability: " + inventory.getRoomAvailability().get("Single"));
  }
}

class CancellationService {
  private Stack<String> releasedRoomIds = new Stack<>();
  private Map<String, String> reservationRoomTypeMap = new HashMap<>();

  public void registerBooking(String reservationId, String roomType) {
    reservationRoomTypeMap.put(reservationId, roomType);
  }

  public void cancelBooking(String reservationId, RoomInventory inventory) {
    if (reservationRoomTypeMap.containsKey(reservationId)) {
      String roomType = reservationRoomTypeMap.remove(reservationId);
      releasedRoomIds.push(reservationId);

      int currentCount = inventory.getRoomAvailability().get(roomType);
      inventory.updateAvailability(roomType, currentCount + 1);
    }
  }

  public void showRollbackHistory() {
    while (!releasedRoomIds.isEmpty()) {
      System.out.println("Rolled back: " + releasedRoomIds.pop());
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
