import java.util.*;

public class BookMyStayApp {
  public static void main(String[] args) {

    RoomInventory inventory = new RoomInventory();
    BookingRequestQueue bookingQueue = new BookingRequestQueue();
    RoomAllocationService allocationService = new RoomAllocationService();
    AddOnServiceManager serviceManager = new AddOnServiceManager();

    inventory.updateAvailability("Single", 5);
    inventory.updateAvailability("Double", 3);
    inventory.updateAvailability("Suite", 2);

    Reservation r1 = new Reservation("Abhi", "Single");
    Reservation r2 = new Reservation("Subha", "Double");
    Reservation r3 = new Reservation("Vanmathi", "Suite");

    bookingQueue.addRequest(r1);
    bookingQueue.addRequest(r2);
    bookingQueue.addRequest(r3);

    System.out.println("--- Processing Room Allocation ---");

    String confirmedId = "";

    while (bookingQueue.hasPendingRequests()) {
      Reservation request = bookingQueue.processNextRequest();

      // allocate room (UC6 logic)
      confirmedId = allocationService.allocateRoom(request, inventory);

      // print booking info (UC earlier logic)
      System.out.println("Processing booking for Guest: " + request.getGuestName() +
          ", Room Type: " + request.getRoomType());
    }

    if (!confirmedId.isEmpty()) {
      System.out.println("--- Add-On Service Selection ---");

      Service breakfast = new Service("Breakfast", 500.0);
      Service spa = new Service("Spa", 1000.0);

      serviceManager.addService(confirmedId, breakfast);
      serviceManager.addService(confirmedId, spa);

      double totalExtra = serviceManager.calculateTotalServiceCost(confirmedId);

      System.out.println("Reservation ID: " + confirmedId);
      System.out.println("Total Add-On Cost: " + totalExtra);
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

class RoomInventory {
  private Map<String, Integer> roomAvailability = new HashMap<>();

  public void updateAvailability(String roomType, int count) {
    roomAvailability.put(roomType, count);
  }

  public Map<String, Integer> getRoomAvailability() {
    return roomAvailability;
  }
}

class RoomAllocationService {
  public String allocateRoom(Reservation reservation, RoomInventory inventory) {
    String type = reservation.getRoomType();
    Map<String, Integer> availability = inventory.getRoomAvailability();

    if (availability.getOrDefault(type, 0) > 0) {
      String roomId = type + "-" + (int) (Math.random() * 100);
      inventory.updateAvailability(type, availability.get(type) - 1);
      System.out.println("Confirmed: Guest " + reservation.getGuestName() + " assigned Room ID: " + roomId);
      return roomId;
    }
    return "";
  }
}

class Service {
  private String serviceName;
  private double cost;

  public Service(String serviceName, double cost) {
    this.serviceName = serviceName;
    this.cost = cost;
  }

  public String getServiceName() {
    return serviceName;
  }

  public double getCost() {
    return cost;
  }
}

class AddOnServiceManager {
  private Map<String, List<Service>> servicesByReservation = new HashMap<>();

  public void addService(String reservationId, Service service) {
    servicesByReservation.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
  }

  public double calculateTotalServiceCost(String reservationId) {
    List<Service> selectedServices = servicesByReservation.getOrDefault(reservationId, new ArrayList<>());
    double total = 0;
    for (Service s : selectedServices) {
      total += s.getCost();
    }
    return total;
  }
}