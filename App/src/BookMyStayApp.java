import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class BookMyStayApp {
  public static void main(String[] args) {
    BookingRequestQueue bookingQueue = new BookingRequestQueue();

    Reservation r1 = new Reservation("Abhi", "Single");
    Reservation r2 = new Reservation("Subha", "Double");
    Reservation r3 = new Reservation("Vanmathi", "Suite");

    bookingQueue.addRequest(r1);
    bookingQueue.addRequest(r2);
    bookingQueue.addRequest(r3);

    System.out.println("--- Booking Request Queue ---");
    while (bookingQueue.hasPendingRequests()) {
      Reservation current = bookingQueue.processNextRequest();
      System.out.println("Processing booking for Guest: " + current.getGuestName() +
          ", Room Type: " + current.getRoomType());
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
  private Queue<Reservation> requests = new LinkedList<>();

  public void addRequest(Reservation request) {
    requests.add(request);
  }

  public Reservation processNextRequest() {
    return requests.poll();
  }

  public boolean hasPendingRequests() {
    return !requests.isEmpty();
  }
}
