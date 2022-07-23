import api.HotelResource;
import model.Iroom;
import model.Reservation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;


public class MainMenu {

    private static final LocalDateTime today = LocalDateTime.now();
    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final String emailRegex = "^(.+)@(.+)\\.com$";
    private static final Pattern pattern = Pattern.compile(emailRegex);
    //private static final Pattern pattern1 = Pattern.compile(dateFormat1);

    public static void printMainMenu() {
        System.out.println("""
                \s
                Welcome to Hotel Udacity's online reservation application\s
                *********************************************************\s
                1. Reserve a room\s
                2. See my reservations\s
                3. Create an account\s
                4. Admin menu\s
                5. Exit application\s
                *********************************************************\s
                Select a number option from the menu above\s
                """);
    }

    public static void mainMenu() {
        String line = "";
        Scanner scanner = new Scanner(System.in);

        printMainMenu();

        try {
            do {
                line = scanner.nextLine();

                if (line.length() == 1) {
                    switch (line.charAt(0)) {
                        case '1':
                            findRoom();
                            break;
                        case '2':
                            seeMyReservation();
                            break;
                        case '3':
                            createAccount();
                            break;
                        case '4':
                            AdminMenu.adminMenu();
                            break;
                        case '5':
                            System.out.println("Exiting program...");
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
            } while (line.charAt(0) != '5' || line.length() != 1);
        } catch (StringIndexOutOfBoundsException ex) {
            System.out.println("Unknown Error... Exiting program...");
        }
    }

    private static void findRoom() {                            // find room
        final Scanner scanner = new Scanner(System.in);
        String totalDays = null;
        String totalNights = null;
        String totalPrice = null;
        final String dateFormat1 = "^\\d{2}/\\d{2}/\\d{4}$";     // prequalify pattern 1
        final Pattern pattern1 = Pattern.compile(dateFormat1);
        final String dateFormat2 = "MM/dd/yyyy";                 // date format pattern
        DateTimeFormatter myFormat2 = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        System.out.println("Enter Check-In Date example: mm/dd/yyyy");
        String inputInDate = scanner.nextLine();
        DateFormat sdfIn = new SimpleDateFormat(dateFormat2);     // new Simple Date Format
        sdfIn.setLenient(false);                                  // setLenient FALSE
        Date inDate = null;
        Date checkIn2 = null;
        Date enteredCheckIn = null;
        if (pattern1.matcher(inputInDate).matches()) {            // checks String against pattern 1
            try {
                inDate = sdfIn.parse(inputInDate);            // parse String with format "MM/dd/yyyy"
                String formatTodaysDate = today.format(myFormat2);
                enteredCheckIn = inDate;
                Date todayDate = null;
                try {
                    todayDate = sdfIn.parse(formatTodaysDate);            // parse String with format "MM/dd/yyyy"
                } catch (ParseException e) {
                    System.out.println("Internal Error!!!");
                }
                assert todayDate != null;
                long validCheckIn = enteredCheckIn.getTime() - todayDate.getTime();
                float daysBetweenin = (validCheckIn / (1000 * 60 * 60 * 24));
                if (daysBetweenin < 0) {
                    System.out.println("Invalid check in date!!!" +         // check in date must be future date
                            "Your check in date cannot be before today");
                    findRoom();
                } else {
                    checkIn2 = enteredCheckIn;
                }
            } catch (ParseException e) {
                System.out.println("Error... Invalid date!!!" +
                        "Please use correct date format example: mm/dd/yyyy");
                findRoom();
            }
        } else {
            System.out.println("Error... Invalid date!!!" +
                    "Please use correct date format example: mm/dd/yyyy");
            findRoom();
        }

        Date checkIn = checkIn2;

        System.out.println("Enter Check-Out Date example: mm/dd/yyyy");
        String inputOutDate = scanner.nextLine();
        DateFormat sdfOut = new SimpleDateFormat(dateFormat2);              // new Simple Date Format
        sdfOut.setLenient(false);                                  // setLenient FALSE
        Date outDate = null;
        Date checkOut2 = null;
        if (pattern1.matcher(inputOutDate).matches()) {            // checks String against pattern 1
            try {
                outDate = sdfOut.parse(inputOutDate);            // parse String with format "MM/dd/yyyy"
                Date enteredCheckOut = outDate;
                long validCheckOut = enteredCheckOut.getTime() - enteredCheckIn.getTime();
                float daysBetween = (validCheckOut / (1000 * 60 * 60 * 24));
                if (daysBetween <= 0) {
                    System.out.println("Invalid check out date!!! \n" +    // check out date must be after check in
                            "Your check out date cannot be before your check in date");
                    findRoom();
                } else {
                    checkOut2 = enteredCheckOut;               // calculates total days and nights of reservation
                    int typechange = (int) daysBetween;
                    totalDays = Integer.toString(typechange);
                    totalNights = Integer.toString(typechange - 1);
                }
            } catch (ParseException e) {
                System.out.println("Error... Invalid date!!! \n" +
                        "Please use correct date format example: mm/dd/yyyy");
                findRoom();
            }
        } else {
            System.out.println("Error... Invalid date!!!" +
                    "Please use correct date format example: mm/dd/yyyy");
            findRoom();
        }

        Date checkOut = checkOut2;

        if (checkIn != null && checkOut != null) {
            Collection<Iroom> availableRooms = hotelResource.findARoom(checkIn, checkOut);

            if (availableRooms.isEmpty()) {
                Collection<Iroom> altRooms = hotelResource.findAltRooms(checkIn, checkOut);

                if (altRooms.isEmpty()) {               // giving option to go straight to search for new dates
                    System.out.println("No available rooms on the dates you selected");
                    mainMenu();
                } else {
                    final Date altCheckIn = hotelResource.addAltDays(checkIn);
                    final Date altCheckOut = hotelResource.addAltDays(checkOut);
                    System.out.println("Sorry the dates you selected are not available \n" +
                            "We do have some availability on these dates instead: \n" +
                            " Check-In Date: \n" + altCheckIn +
                            " Check-Out Date: " + altCheckOut);

                    printRooms(altRooms);
                    System.out.println("Would you like to book one of these rooms? Y/N");
                    while (true) {          //while loop so customer doesn't have to start over with invalid entry
                        try {
                            final String doBookAlt = scanner.nextLine();
                            if ("Y".equalsIgnoreCase(doBookAlt)) {
                                break;
                            } else if ("N".equalsIgnoreCase(doBookAlt)) {
                                mainMenu();
                                break;
                            } else if
                            ((!"y".equalsIgnoreCase(doBookAlt)) || (!"n".equalsIgnoreCase(doBookAlt))) {
                                System.out.println("Error... Invalid Entry!!! \n" +
                                        "Please enter Y \"Yes\" or N \"No\"");
                            }
                        } catch (IllegalArgumentException ignore) {
                            System.out.println("Error... Invalid Entry!!! \n" +
                                    "Please enter Y \"Yes\" or N \"No\"");
                        }
                    }
                    System.out.println("Do you have an account? Y/N");
                    while (true) {      //while loop so customer doesn't have to start over with invalid entry
                        try {
                            String haveAccount = scanner.nextLine();
                            if ("Y".equalsIgnoreCase(haveAccount)) {
                                reserveRoom(scanner,
                                        altCheckIn,
                                        altCheckOut,
                                        totalDays,
                                        totalNights,
                                        totalPrice,
                                        altRooms);
                                break;
                            } else if ("n".equalsIgnoreCase(haveAccount)) {
                                System.out.println("Please create an account to continue...");
                                createAccount();  // sends customer directly to account creation
                                break;
                            } else {
                                System.out.println("Error... Invalid Entry!!! \n" +
                                        "Please enter Y \"Yes\" or N \"No\"");
                            }
                        } catch (IllegalArgumentException ex) {
                            System.out.println("Error... Invalid Entry!!!");
                        }
                    }
                }
            } else {
                printRooms(availableRooms);
                System.out.println("Would you like to book a room? Y/N");
                while (true) {      //while loop so customer doesn't have to start over with invalid entry
                    try {
                        String bookRoom = scanner.nextLine();
                        if ("y".equalsIgnoreCase(bookRoom)) {
                            break;
                        } else if ("n".equalsIgnoreCase(bookRoom)) {
                            mainMenu();
                            break;
                        } else {
                            System.out.println("Error... Invalid Entry!!! \n" +
                                    "Please enter Y \"Yes\" or N \"No\"");
                        }
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Error... Invalid Entry!!! \n" +
                                "Please enter Y \"Yes\" or N \"No\"");
                    }
                }

                System.out.println("Do you have an account with us? Y/N");
                while (true) {       //while loop so customer doesn't have to start over with invalid entry
                    try {
                        String haveAccount = scanner.nextLine();
                        if ("y".equalsIgnoreCase(haveAccount)) {
                            reserveRoom(scanner,
                                    checkIn,
                                    checkOut,
                                    totalDays,
                                    totalNights,
                                    totalPrice,
                                    availableRooms);
                            break;
                        } else if ("n".equalsIgnoreCase(haveAccount)) {
                            System.out.println("Please create an account to continue...");
                            createAccount();  // sends customer directly to account creation
                            break;
                        } else {
                            System.out.println("Error... Invalid Entry!!! \n" +
                                    "Please enter Y \"Yes\" or N \"No\"");
                        }
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Error... Invalid Entry!!! \n" +
                                "Please enter Y \"Yes\" or N \"No\"");
                    }
                }

            }
        } else {
            System.out.println("Sorry there are no rooms available at this time");
        }
    }

    private static void reserveRoom(final Scanner scanner,          // Reserve Room
                                    final Date checkInDate,
                                    final Date checkOutDate,
                                    final String totalDays,
                                    final String totalNights,
                                    String totalPrice,
                                    final Collection<Iroom> rooms) {

        String customerEmail = validateEmail(scanner);          // made a separate validate email function below
        printRooms(rooms);
        System.out.println("What room number would you like to reserve? ");
        int roomNum = 0;
        while(true) {
            try {
                roomNum = Integer.parseInt(scanner.nextLine());  // https://www.dotnetperls.com/parseint-java
                break;
            } catch (NumberFormatException ignore) {
                System.out.println("Error: Invalid Entry!!! \n" +
                        "Please select from the available room choices \n" +
                        "No letters or special characters allowed");
            }
        }
        final String roomNumber = Integer.toString(roomNum);                // calculates the total cost of reservation
        if (rooms.stream().anyMatch(room -> room.getRoomNumber().equals(roomNumber))) {
            final Iroom room = hotelResource.getRoom(roomNumber);
            double doubletotalNights = Double.valueOf(totalNights);
            totalPrice = Double.toString(room.getRoomPrice() * doubletotalNights);


            final Reservation reservation = hotelResource
                    .bookARoom(customerEmail,
                            room,
                            checkInDate,
                            checkOutDate,
                            totalDays,
                            totalNights,
                            totalPrice);
            System.out.println("You're room is booked!!!");
            System.out.println(reservation);
        } else {
            System.out.println("Error... room number not available. \n" +
                    "Please sign in again to continue...");
            reserveRoom(scanner, checkInDate, checkOutDate, totalDays, totalNights, totalPrice, rooms);
        }
        mainMenu();
    }

    public static String validateEmail (final Scanner scanner) {  // Validate email
        int line = 0;
        String customerEmail = null;
        System.out.println("Please enter your email address");
        String enterCustomerEmail = scanner.nextLine();
        if (!pattern.matcher(enterCustomerEmail).matches()) {
            System.out.println("Error, Please use a valid email... example: \"name@domain.com\"");
            validateEmail(scanner);
        }
        else {
            customerEmail = enterCustomerEmail;

            if (hotelResource.getCustomer(customerEmail) == null) {
                System.out.println("Customer not found..." +
                        "You may need to create a new account... ");
                MainMenu.mainMenu();

              /*  try {
                    Scanner doTryAgain = new Scanner(System.in);
                    while (line !=3) {
                        while (true) {                      // https://www.dotnetperls.com/parseint-java
                            try { line = Integer.parseInt(scanner.nextLine());
                                break;
                            } catch (NumberFormatException ignore) {
                                System.out.println("Error: Invalid Entry!!! \n" +
                                        "Please select from the Menu choices \n" +
                                        "No letters or special characters allowed");
                            }
                        }
                        if (line == 1) {
                            createAccount();
                            break;
                        } else if (line == 2) {
                            validateEmail(scanner);
                            break;
                        } else if (line == 3) {
                            printMainMenu();
                            break;
                        } else {
                            System.out.println("Error: Invalid Entry!!! \n" +
                                    "Please select from the Menu choices \n" +
                                    "No letters or special characters allowed");
                        }
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("An error occurred... Exiting program...");
                } */
            } else {
                return customerEmail;
            }
        }
        return customerEmail;
    }

    private static void createAccount() {           // account creation
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter your email address");
        final String email = scanner.nextLine();
        if(!pattern.matcher(email).matches()) {
            System.out.println("Error, Please use a valid email... example: \"name@domain.com\"");
            createAccount();
        }

        System.out.println("Please enter your First Name:");
        final String firstName = scanner.nextLine();

        System.out.println("Please enter your Last Name:");
        final String lastName = scanner.nextLine();

        try {
            hotelResource.createACustomer(email,
                    firstName,
                    lastName);
            System.out.println("Account created successfully!");
            mainMenu();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage() +
                    "\n An error occurred... Please Try again");
            createAccount();
        }
    }

    private static void printRooms(final Collection<Iroom> rooms) {     //print available rooms
        if (rooms == null || rooms.isEmpty()) {
            System.out.println("No rooms found on those dates.");
        } else {
            //rooms.forEach(room -> System.out.println(room));   // started with this
            rooms.forEach(System.out::println);          // was prompted to replace lambda with this
        }
    }

    private static void seeMyReservation() {                // input to find reservation
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your email address");
        String customerEmail = scanner.nextLine();
        printReservations(hotelResource.getCustomersReservations(customerEmail));
        mainMenu();
    }

    private static void printReservations(final Collection<Reservation> reservations) {     // print reservation
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found using the email you provided.");
        } else {
            //reservations.forEach(reservation -> System.out.println(reservation)); // started with this
            reservations.forEach(System.out::println);                     // was prompted to replace lambda with this

        }
    }
}