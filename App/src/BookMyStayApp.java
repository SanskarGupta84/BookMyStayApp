import java.util.*;

public class BookMyStayApp {
  public static void main(String[] args) {
    RoomInventory inventory = new RoomInventory();
    BookingRequestQueue bookingQueue = new BookingRequestQueue();
    RoomAllocationService allocationService = new RoomAllocationService();
    BookingHistory history = new BookingHistory();
    BookingReportService reportService = new BookingReportService();

    inventory.updateAvailability("Single", 5);
    inventory.updateAvailability("Double", 3);
    inventory.updateAvailability("Suite", 2);

    bookingQueue.addRequest(new Reservation("Abhi", "Single"));
    bookingQueue.addRequest(new Reservation("Subha", "Double"));
    bookingQueue.addRequest(new Reservation("Vanmathi", "Suite"));

    while (bookingQueue.hasPendingRequests()) {
      Reservation request = bookingQueue.processNextRequest();
      String confirmedId = allocationService.allocateRoom(request, inventory);
      if (!confirmedId.isEmpty()) {
        history.addReservation(request);
      }
    }

    reportService.generateReport(history);
  }
}

class BookingHistory {
  private List<Reservation> confirmedReservations = new ArrayList<>();

  public void addReservation(Reservation reservation) {
    confirmedReservations.add(reservation);
  }

  public List<Reservation> getConfirmedReservations() {
    return confirmedReservations;
  }
}

class BookingReportService {
  public void generateReport(BookingHistory history) {
    System.out.println("--- Booking History Report ---");
    for (Reservation r : history.getConfirmedReservations()) {
      System.out.println("Guest: " + r.getGuestName() + ", Room Type: " + r.getRoomType());
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
  public String allocateRoom(Reservation reservation, RoomInventory inventory) {
    String type = reservation.getRoomType();
    Map<String, Integer> availability = inventory.getRoomAvailability();

    if (availability.getOrDefault(type, 0) > 0) {
      String roomId = type + "-" + (int) (Math.random() * 100);
      inventory.updateAvailability(type, availability.get(type) - 1);
      return roomId;
    }
    return "";
  }
}
