import java.util.*;

public class BookMyStayApp {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    RoomInventory inventory = new RoomInventory();
    ReservationValidator validator = new ReservationValidator();
    BookingRequestQueue bookingQueue = new BookingRequestQueue();

    inventory.updateAvailability("Single", 5);
    inventory.updateAvailability("Double", 3);
    inventory.updateAvailability("Suite", 2);

    System.out.println("Booking Validation");
    try {
      System.out.print("Enter guest name: ");
      String guestName = scanner.nextLine();
      System.out.print("Enter room type (Single/Double/Suite): ");
      String roomType = scanner.nextLine();

      validator.validate(guestName, roomType, inventory);
      bookingQueue.addRequest(new Reservation(guestName, roomType));
    } catch (InvalidBookingException e) {
      System.out.println("Booking failed: " + e.getMessage());
    } finally {
      scanner.close();
    }
  }
}

class InvalidBookingException extends Exception {
  public InvalidBookingException(String message) {
    super(message);
  }
}

class ReservationValidator {
  public void validate(String guestName, String roomType, RoomInventory inventory) throws InvalidBookingException {
    if (guestName == null || guestName.trim().isEmpty()) {
      throw new InvalidBookingException("Guest name cannot be empty.");
    }

    Map<String, Integer> availability = inventory.getRoomAvailability();
    if (!availability.containsKey(roomType)) {
      throw new InvalidBookingException("Invalid room type selected.");
    }

    if (availability.get(roomType) <= 0) {
      throw new InvalidBookingException("No availability for the selected room type.");
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
}
