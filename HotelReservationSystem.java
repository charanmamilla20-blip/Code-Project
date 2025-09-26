import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Main class to manage the hotel system logic and user interface.
 * All helper classes and enums are nested here to ensure the compiler
 * correctly finds the main method.
 */
public class HotelReservationSystem {

    /**
     * Defines the available room categories, including their default pricing.
     */
    public static enum RoomCategory {
        STANDARD(100.00),
        DELUXE(150.00),
        SUITE(250.00);

        private final double basePrice;

        RoomCategory(double basePrice) {
            this.basePrice = basePrice;
        }

        public double getBasePrice() {
            return basePrice;
        }

        @Override
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }

    /**
     * Represents a single hotel room.
     */
    public static class Room {
        private final String roomNumber;
        private final RoomCategory category;
        private double pricePerNight;
        private boolean isAvailable;

        public Room(String roomNumber, RoomCategory category) {
            this.roomNumber = roomNumber;
            this.category = category;
            this.pricePerNight = category.getBasePrice();
            this.isAvailable = true;
        }

        // Getters and Setters
        public String getRoomNumber() { return roomNumber; }
        public RoomCategory getCategory() { return category; }
        public double getPricePerNight() { return pricePerNight; }
        public boolean isAvailable() { return isAvailable; }

        public void setAvailable(boolean available) {
            isAvailable = available;
        }

        @Override
        public String toString() {
            return String.format("Room %s (%s) - $%.2f/night [%s]",
                    roomNumber, category.toString(), pricePerNight, isAvailable ? "AVAILABLE" : "OCCUPIED");
        }
    }

    /**
     * Represents a reservation made by a guest.
     */
    public static class Reservation {
        private final String bookingId;
        private final Room room;
        private final String guestName;
        private final int numberOfNights;
        private final double totalCost;

        public Reservation(Room room, String guestName, int numberOfNights) {
            this.bookingId = UUID.randomUUID().toString().substring(0, 8).toUpperCase(); // Simple unique ID
            this.room = room;
            this.guestName = guestName;
            this.numberOfNights = numberOfNights;
            this.totalCost = room.getPricePerNight() * numberOfNights;
        }

        // Getters
        public String getBookingId() { return bookingId; }
        public Room getRoom() { return room; }
        public String getGuestName() { return guestName; }
        public int getNumberOfNights() { return numberOfNights; }
        public double getTotalCost() { return totalCost; }

        /**
         * Provides formatted summary of the reservation.
         */
        public String getDetails() {
            return String.format("--- BOOKING DETAILS ---\n" +
                                 "ID: %s\n" +
                                 "Guest: %s\n" +
                                 "Room: %s (%s)\n" +
                                 "Nights: %d\n" +
                                 "Cost/Night: $%.2f\n" +
                                 "Total Paid: $%.2f",
                                 bookingId, guestName, room.getRoomNumber(),
                                 room.getCategory().toString(), numberOfNights,
                                 room.getPricePerNight(), totalCost);
        }
    }


    private List<Room> rooms;
    private List<Reservation> reservations;
    private Scanner scanner;

    public HotelReservationSystem() {
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        initializeRooms();
    }

    /**
     * Initializes the hotel rooms for the system.
     */
    private void initializeRooms() {
        // Standard Rooms
        for (int i = 101; i <= 105; i++) {
            rooms.add(new Room(String.valueOf(i), RoomCategory.STANDARD));
        }
        // Deluxe Rooms
        for (int i = 201; i <= 203; i++) {
            rooms.add(new Room(String.valueOf(i), RoomCategory.DELUXE));
        }
        // Suite Rooms
        for (int i = 301; i <= 302; i++) {
            rooms.add(new Room(String.valueOf(i), RoomCategory.SUITE));
        }
        System.out.println("Hotel system initialized with " + rooms.size() + " rooms.");
    }

    /**
     * Displays the main menu and runs the user interaction loop.
     */
    public void run() {
        int choice = -1;
        System.out.println("\n=============================================");
        System.out.println("     🏨 Welcome to the Cloud Hotel System 🏨");
        System.out.println("=============================================");

        while (choice != 5) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Search Available Rooms");
            System.out.println("2. Make a Reservation");
            System.out.println("3. View Booking Details");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. Exit System");
            System.out.print("Enter choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        searchRooms();
                        break;
                    case 2:
                        makeReservation();
                        break;
                    case 3:
                        viewBookingDetails();
                        break;
                    case 4:
                        cancelReservation();
                        break;
                    case 5:
                        System.out.println("Thank you for using the Cloud Hotel System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a number from 1 to 5.");
                }
            } catch (InputMismatchException e) {
                System.out.println("⚠️ Invalid input. Please enter a valid menu number.");
                scanner.nextLine(); // Clear the invalid input
                choice = -1;
            }
        }
    }

    /**
     * Allows the user to search for available rooms by category.
     */
    private void searchRooms() {
        System.out.println("\n--- ROOM SEARCH ---");
        System.out.println("Categories: 1. Standard ($100) | 2. Deluxe ($150) | 3. Suite ($250)");
        System.out.print("Enter category number (1-3) or 0 for ALL: ");
        int categoryChoice = -1;

        try {
            categoryChoice = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("⚠️ Invalid input. Returning to main menu.");
            scanner.nextLine();
            return;
        }

        final RoomCategory targetCategory;
        
        switch (categoryChoice) {
            case 1: targetCategory = RoomCategory.STANDARD; break;
            case 2: targetCategory = RoomCategory.DELUXE; break;
            case 3: targetCategory = RoomCategory.SUITE; break;
            case 0: targetCategory = null; break; // Explicitly assign null for ALL rooms
            default:
                System.out.println("⚠️ Invalid category choice.");
                return; 
        }

        List<Room> availableRooms = rooms.stream()
                .filter(Room::isAvailable)
                .filter(r -> targetCategory == null || r.getCategory() == targetCategory)
                .collect(Collectors.toList());

        if (availableRooms.isEmpty()) {
            System.out.println("❌ No available rooms match your criteria.");
        } else {
            System.out.println("\n--- AVAILABLE ROOMS ---");
            availableRooms.forEach(System.out::println);
        }
    }

    /**
     * Handles the process of making a new reservation.
     */
    private void makeReservation() {
        System.out.println("\n--- NEW RESERVATION ---");
        
        // 1. Search for available rooms first
        List<Room> availableRooms = rooms.stream().filter(Room::isAvailable).collect(Collectors.toList());
        if (availableRooms.isEmpty()) {
            System.out.println("❌ The hotel is currently fully booked. Cannot make a reservation.");
            return;
        }

        System.out.println("Available Rooms:");
        availableRooms.forEach(r -> System.out.println("  " + r.getRoomNumber() + ": " + r.getCategory().toString() + " ($" + r.getPricePerNight() + ")"));

        // 2. Room Selection
        System.out.print("Enter the Room Number you wish to book: ");
        String roomNum = scanner.nextLine().trim();

        Optional<Room> selectedRoomOpt = rooms.stream()
                .filter(r -> r.getRoomNumber().equalsIgnoreCase(roomNum) && r.isAvailable())
                .findFirst();

        if (selectedRoomOpt.isEmpty()) {
            System.out.println("❌ Room " + roomNum + " is invalid or not available.");
            return;
        }
        Room selectedRoom = selectedRoomOpt.get();

        // 3. Guest and Duration Input
        System.out.print("Enter your Name for the booking: ");
        String guestName = scanner.nextLine().trim();
        if (guestName.isEmpty()) {
            System.out.println("❌ Guest name cannot be empty. Reservation aborted.");
            return;
        }

        int nights = 0;
        try {
            System.out.print("Enter number of nights to stay (1-30): ");
            nights = scanner.nextInt();
            scanner.nextLine();
            if (nights <= 0 || nights > 30) throw new InputMismatchException();
        } catch (InputMismatchException e) {
            System.out.println("❌ Invalid number of nights. Reservation aborted.");
            scanner.nextLine();
            return;
        }

        // 4. Payment Simulation
        double cost = selectedRoom.getPricePerNight() * nights;
        System.out.printf("\n--- PAYMENT CONFIRMATION ---\n" +
                          "Room: %s (%s) @ $%.2f/night\n" +
                          "Nights: %d\n" +
                          "Total Cost: $%.2f\n" +
                          "Confirm payment (yes/no)? ",
                          selectedRoom.getRoomNumber(), selectedRoom.getCategory().toString(),
                          selectedRoom.getPricePerNight(), nights, cost);

        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("yes")) {
            System.out.println("Reservation cancelled by user during payment simulation.");
            return;
        }

        // 5. Finalize Booking
        selectedRoom.setAvailable(false);
        Reservation newReservation = new Reservation(selectedRoom, guestName, nights);
        reservations.add(newReservation);

        System.out.println("\n🎉 RESERVATION SUCCESSFUL!");
        System.out.println(newReservation.getDetails());
    }

    /**
     * Allows the user to view the details of an existing booking.
     */
    private void viewBookingDetails() {
        System.out.println("\n--- VIEW BOOKING ---");
        System.out.print("Enter your Booking ID: ");
        String id = scanner.nextLine().trim().toUpperCase();

        Optional<Reservation> reservationOpt = reservations.stream()
                .filter(r -> r.getBookingId().equals(id))
                .findFirst();

        if (reservationOpt.isPresent()) {
            System.out.println(reservationOpt.get().getDetails());
        } else {
            System.out.println("❌ Booking ID " + id + " not found.");
        }
    }

    /**
     * Allows the user to cancel an existing reservation.
     */
    private void cancelReservation() {
        System.out.println("\n--- CANCEL RESERVATION ---");
        System.out.print("Enter the Booking ID to cancel: ");
        String id = scanner.nextLine().trim().toUpperCase();

        Optional<Reservation> reservationOpt = reservations.stream()
                .filter(r -> r.getBookingId().equals(id))
                .findFirst();

        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            System.out.printf("Are you sure you want to cancel booking ID %s for %s (Room %s)? (yes/no): ",
                    reservation.getBookingId(), reservation.getGuestName(), reservation.getRoom().getRoomNumber());

            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("yes")) {
                // 1. Update Room Availability
                reservation.getRoom().setAvailable(true);

                // 2. Remove Reservation
                reservations.remove(reservation);

                System.out.printf("✅ Reservation %s successfully cancelled. Room %s is now available.\n",
                        id, reservation.getRoom().getRoomNumber());
            } else {
                System.out.println("Cancellation aborted.");
            }
        } else {
            System.out.println("❌ Booking ID " + id + " not found.");
        }
    }

    /**
     * Main method to start the application.
     */
    public static void main(String[] args) {
        HotelReservationSystem system = new HotelReservationSystem();
        system.run();
    }
}
