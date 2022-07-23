import api.AdminResource;
import model.Customer;
import model.Iroom;
import model.Room;
import model.RoomType;
import model.FreeRoom;

import java.util.*;

import static model.RoomType.*;

/** Admin Menu
 *
 * @author Jason Renek
 */
public class AdminMenu {

    private static final AdminResource adminResource = AdminResource.getInstance();

    private static void printMenu() {                   // ADMIN MENU USER INTERFACE
        System.out.print("""

                Admin Menu
                *******************************************
                1. See all Customers
                2. See all Rooms
                3. See all Reservations
                4. Add a Room
                5. Create test Data
                6. Back to Main Menu
                *******************************************
                Please select a number for the menu option:
                """);
    }

    public static void adminMenu() {
        String line = "";
        final Scanner scanner = new Scanner(System.in);

        printMenu();

        try {
            do {
                line = scanner.nextLine();

                if (line.length() == 1) {
                    switch (line.charAt(0)) {
                        case '1':
                            displayAllCustomers();
                            break;
                        case '2':
                            displayAllRooms();
                            break;
                        case '3':
                            displayAllReservations();
                            break;
                        case '4':
                            addRoom();
                            break;
                        case '5':
                            createTestData();
                            break;
                        case '6':
                            MainMenu.mainMenu();
                            break;
                        default:
                            System.out.println("Error: Invalid Entry!!! \n" +
                                    "Please select from the Menu choices \n" +
                                    "No letters or special characters allowed");
                            break;
                    }
                } else {
                    System.out.println("Error: Invalid Entry!!! \n" +
                            "Please select from the Menu choices \n" +
                            "No letters or special characters allowed");
                }
            } while (line.charAt(0) != '6' || line.length() != 1);
        } catch (StringIndexOutOfBoundsException ex) {
            System.out.println("Unknown Error... Exiting program...");
        }
    }

    private static void addRoom() {                         // ADD ROOM  the 1st way I wrote it
        final Scanner scanner = new Scanner(System.in);
        int roomNumberInt = 0;
        System.out.println("Enter room number:");   // force room number entry as int
        while (true) {
            try {
                roomNumberInt = Integer.parseInt(scanner.nextLine());  // https://www.dotnetperls.com/parseint-java
                break;
            } catch (NumberFormatException ignore) {
                System.out.println("Error: Invalid Entry!!! \n" +
                        "Please select from the Menu choices \n" +
                        "No letters or special characters allowed");
            }
        }
        final String roomNumber = Integer.toString(roomNumberInt);  // converted back to string

        System.out.println("Enter price per night:");
        double enterRoomPrice = 0.0;
        while (true) {
            try {                                                       // force room price entry as double
                enterRoomPrice = Double.parseDouble(scanner.nextLine());  // https://www.dotnetperls.com/parseint-java
                break;
            } catch (NumberFormatException ignore) {
                System.out.println("Error: Invalid Entry!!! \n" +
                        "Please select from the Menu choices \n" +
                        "No letters or special characters allowed");
            }
        }
        final double roomPrice = enterRoomPrice;
        boolean isFree = false;
        if (roomPrice == 0) {
            isFree = true;
        }

        System.out.println("Enter room type: 1 for single bed, 2 for double bed:");

        final RoomType enumeration = enterRoomType(scanner);
        final Room room;

        if (!isFree) {
            room = new Room(roomNumber,      // if price > 0 add room to collection
                    roomPrice,
                    enumeration);

            // https://www.geeksforgeeks.org/collections-singletonlist-method-in-java-with-examples/
        } else {
            room = new FreeRoom(roomNumber,  // if price = 0 add free room to collection
                    roomPrice,
                    enumeration);

            // https://www.geeksforgeeks.org/collections-singletonlist-method-in-java-with-examples/
        }
        System.out.println("Room # " + roomNumber + " Added");
        adminResource.addRoom(Collections.singletonList(room));

        System.out.println("Would like to add another room? Y/N");
        addAnotherRoom();
    }

    private static void addAnotherRoom() {
        final Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                String addRoom = scanner.nextLine();
                if ("y".equalsIgnoreCase(addRoom)) {
                    addRoom();
                    break;
                } else if ("n".equalsIgnoreCase(addRoom)) {
                    adminMenu();
                    break;
                } else {
                    System.out.println("Error... Invalid Entry!!! \n" +
                            "Please enter Y \"Yes\" or N \"No\"");
                }
            } catch (StringIndexOutOfBoundsException ex) {
                System.out.println("Error... Invalid Entry!!! \n" +
                        "Please enter Y \"Yes\" or N \"No\"");
            }
        }
    }

    private static RoomType enterRoomType(final Scanner scanner) {
        try {
            return RoomType.valueOfLabel(scanner.nextLine());   // https://www.dotnetperls.com/parseint-java
        } catch (IllegalArgumentException exp) {
            System.out.println("Invalid Entry! \n" +
                    "Please, choose 1 for single bed or 2 for double bed:");
            return enterRoomType(scanner);
        }
    }

    private static void displayAllRooms() {                         // DISPLAY ALL ROOMS
        Collection<Iroom> rooms = adminResource.getAllRooms();

        //if(rooms == null || rooms.isEmpty()) {        // started with this but null wasn't needed
        if(rooms.isEmpty()) {
            System.out.println("No rooms found");
            adminMenu();
        } else {
            //adminResource.getAllRooms().forEach(room -> System.out.println(room));  // started with this was
            adminResource.getAllRooms().forEach(System.out::println);  // prompted to replace lambda to this
            adminMenu();
        }
    }

    private static void displayAllCustomers() {                     // DISPLAY ALL CUSTOMERS
        Collection<Customer> customers = adminResource.getAllCustomers();

        //if (customers == null || customers.isEmpty()) {  // started with this but null wasn't needed
        if (customers.isEmpty()) {
            System.out.println("No customers found");
            adminMenu();
        } else {
            //adminResource.getAllCustomers().forEach(customer -> System.out.println(customer)); // started with this
            adminResource.getAllCustomers().forEach(System.out::println);  // was prompted to replace lambda to this
            adminMenu();
        }
    }

    private static void createTestData() {              // CREATE TEST DATA

        Random random = new Random();
        boolean isFree = false;
        RoomType[] ranTypes = {SINGLE, DOUBLE};
        RoomType ranType;
        int j = random.nextInt(15)+5;           // Generate a random number of Rooms

        for(int i = 0; i < j; i++ ) {

            int x = random.nextInt(150)+100;            // Generate a random room numbers
            final String roomNumber = Integer.toString(x);    // Can generate duplicate rooms in this step
            // But have additional step to remove in room model
            int ranRoomPrice = random.nextInt(250);        // Generate random room prices
            double roomPrice = (ranRoomPrice + 0.99);
            isFree = false;
            if(roomPrice < 25) {
                roomPrice = 0;
                isFree = true;
            }

            //ranType = ranTypes[(int) (Math.random() * ranTypes.length)]; // Generate random room types
            final RoomType enumeration = ranTypes[(int)(Math.random() * ranTypes.length)]; // Generate random room types
            final Room room;
            if(!isFree) {                           // if room price > 0 add room to collection
                room = new Room(roomNumber,
                        roomPrice,
                        enumeration);

                // https://www.geeksforgeeks.org/collections-singletonlist-method-in-java-with-examples/
            } else {
                room = new FreeRoom(roomNumber,     // if room price = 0 add free room to collection
                        roomPrice,
                        enumeration);

                // https://www.geeksforgeeks.org/collections-singletonlist-method-in-java-with-examples/
            }
            System.out.println("Room # " + roomNumber + " Added");
            adminResource.addRoom(Collections.singletonList(room));
        }
        adminMenu();
    }

    private static void displayAllReservations() {              // DISPLAY ALL RESERVATIONS
        adminResource.displayAllReservations();
        adminMenu();
    }
}
