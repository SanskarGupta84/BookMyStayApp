import java.util.*;
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
    bookingQueue.addRequest(new Reservation("Subha", "Single"));
    bookingQueue.addRequest(new Reservation("Vanmathi", "Double"));
    bookingQueue.addRequest(new Reservation("Kural", "Suite"));

    ConcurrentBookingProcessor processor = new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService);

    Thread t1 = new Thread(processor);
    Thread t2 = new Thread(processor);

    t1.start();
    t2.start();

    try {
      t1.join(2000);
      t2.join(2000);
    } catch (InterruptedException e) {
      System.out.println("Thread execution interrupted.");
    }

    System.out.println("Remaining Inventory:");
    inventory.getRoomAvailability().forEach((type, count) -> System.out.println(type + ": " + count));
  }
}

class ConcurrentBookingProcessor implements Runnable {
  private BookingRequestQueue bookingQueue;
  private RoomInventory inventory;
  private RoomAllocationService allocationService;

  public ConcurrentBookingProcessor(BookingRequestQueue bookingQueue, RoomInventory inventory,
      RoomAllocationService allocationService) {
    this.bookingQueue = bookingQueue;
    this.inventory = inventory;
    this.allocationService = allocationService;
  }

  @Override
  public void run() {
    while (true) {
      Reservation reservation = null;
      synchronized (bookingQueue) {
        if (bookingQueue.hasPendingRequests()) {
          reservation = bookingQueue.processNextRequest();
        } else {
          break;
        }
      }

      if (reservation != null) {
        synchronized (inventory) {
          allocationService.allocateRoom(reservation, inventory);
        }
      }
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
  public void allocateRoom(Reservation res, RoomInventory inv) {
    Map<String, Integer> avail = inv.getRoomAvailability();
    if (avail.getOrDefault(res.getRoomType(), 0) > 0) {
      avail.put(res.getRoomType(), avail.get(res.getRoomType()) - 1);
      System.out.println("Booking confirmed for Guest: " + res.getGuestName() + ", Room ID: " + res.getRoomType() + "-"
          + (avail.get(res.getRoomType()) + 1));
    }
  }
}
