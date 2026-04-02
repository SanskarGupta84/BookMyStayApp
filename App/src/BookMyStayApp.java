import java.util.*;

public class BookMyStayApp {
  public static void main(String[] args) {
    RoomInventory inventory = new RoomInventory();
    BookingRequestQueue bookingQueue = new BookingRequestQueue();
    RoomAllocationService allocationService = new RoomAllocationService();

    inventory.updateAvailability("Single", 5);
    inventory.updateAvailability("Double", 3);
    inventory.updateAvailability("Suite", 2);

    bookingQueue.addRequest(new Reservation("Abhi", "Single"));
    bookingQueue.addRequest(new Reservation("Subha", "Double"));
    bookingQueue.addRequest(new Reservation("Vanmathi", "Suite"));

    System.out.println("--- Processing Room Allocations ---");

    while (bookingQueue.hasPendingRequests()) {
      Reservation request = bookingQueue.processNextRequest();
      allocationService.allocateRoom(request, inventory);
    }
  }
}

class Reservation {
  private String guestName;
  private String roomType;

  public Reservation(String guestName, String roomType) {
    this.guestName = guestName;
    this.roomType = roomType;
  }

  public String getGuestName() {
    return guestName;
  }

  public String getRoomType() {
    return roomType;
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

class BookingRequestQueue {
  private Queue<Reservation> requestQueue = new LinkedList<>();

  public void addRequest(Reservation reservation) {
    requestQueue.offer(reservation);
  }

  public Reservation processNextRequest() {
    return requestQueue.poll();
  }

  public boolean hasPendingRequests() {
    return !requestQueue.isEmpty();
  }
}

class RoomAllocationService {
  private Set<String> allocatedRoomIds = new HashSet<>();
  private Map<String, Set<String>> assignedRoomsByType = new HashMap<>();

  public void allocateRoom(Reservation reservation, RoomInventory inventory) {
    String type = reservation.getRoomType();
    Map<String, Integer> availability = inventory.getRoomAvailability();

    if (availability.getOrDefault(type, 0) > 0) {
      String roomId = generateRoomId(type);
      allocatedRoomIds.add(roomId);
      assignedRoomsByType.computeIfAbsent(type, k -> new HashSet<>()).add(roomId);

      inventory.updateAvailability(type, availability.get(type) - 1);

      System.out.println("Confirmed: Guest " + reservation.getGuestName() +
          " assigned " + type + " Room ID: " + roomId);
    } else {
      System.out.println("Failed: No availability for " + type + " Room for " + reservation.getGuestName());
    }
  }

  private String generateRoomId(String roomType) {
    return roomType.substring(0, 1).toUpperCase() + "-" + (int) (Math.random() * 1000);
  }
}
