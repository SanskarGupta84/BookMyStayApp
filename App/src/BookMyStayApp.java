public class BookMyStayApp {
  public static void main(String[] args) {
    SingleRoom single = new SingleRoom();
    DoubleRoom doubleRm = new DoubleRoom();
    SuiteRoom suite = new SuiteRoom();

    int singleAvailable = 5;
    int doubleAvailable = 3;
    int suiteAvailable = 2;

    System.out.println("Single Room:");
    single.displayDetails();
    System.out.println("Available: " + singleAvailable + "\n");

    System.out.println("Double Room:");
    doubleRm.displayDetails();
    System.out.println("Available: " + doubleAvailable + "\n");

    System.out.println("Suite Room:");
    suite.displayDetails();
    System.out.println("Available: " + suiteAvailable);
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
