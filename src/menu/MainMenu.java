package menu;

import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

import java.util.*;

public class MainMenu {
    //Menu constants
    private static final String WELCOME_MESSAGE = "" +
            "Welcome to the Udacity Hotel Reservation Application";

    public static String getMainMenu(){
        return MAIN_MENU;
    }
    private static final String UNDERLINE = "-------------------------------";
    private static final String MAIN_MENU = """
            1. Find and reserve a room
            2. See my reservations
            3. Create an account
            4. Admin
            5. Exit
            """;
    private static final String ROOM_MENU = """
            1. Reserve room
            2. Enter new dates
            3. Return to the main menu
            0. Exit
            """;
    private static final String ACCOUNT_MENU = """
            1. Log in to existing account
            2. Create new account
            3. Return to the main menu
            """;
    private static final String RETURN_MENU = """
            1. Return to the main menu
            0. Exit
            """;
    private static final String INVALID_OPTION = "Please choose a valid number from the list.";
    private static final String ACCOUNT_REQUIRED = "A customer account is required in order to complete this step. " +
            "Kindly select one of the options below: ";

    // Date constants
    private static final Date today = new Date();
    private static final Date oneYearFromToday = Date.from(
            LocalDate.now().plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

    private static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
    private static Date checkInDate;

    public static Date getCheckInDate() {
        return checkInDate;
    }

    public static void setCheckInDate(Date checkInDate) {
        MainMenu.checkInDate = checkInDate;
    }

    public static Date getCheckOutDate() {
        return checkOutDate;
    }

    public static void setCheckOutDate(Date checkOutDate) {
        MainMenu.checkOutDate = checkOutDate;
    }

    private static Date checkOutDate;

    // Scanner
    private static Scanner scanner = new Scanner(System.in);

    // Customer in the session
    private static Customer customer;
    public static void setCustomer(Customer customer) {
        MainMenu.customer = customer;
    }

    public static Customer getCustomer() {
        return customer;
    }

    // Current reservation
    static IRoom chosenRoom;

    // MAIN METHODS

    // Show main menu and option selection
    public static void showMenu(String MENU){
        Optional<String> input = Optional.ofNullable(MENU);
        String menu = input.isPresent() ? input.get() : MAIN_MENU;
        System.out.println(MENU);
    }
    public static int selectOption(String MENU, int rangeMinimum, int rangeMaximum) {
        int option = 0;

        boolean optionIsValid = false;

        while (!optionIsValid) {
            showMenu(MENU);
            System.out.println("Select your option: ");

            try {
                option = scanner.nextInt();
                optionIsValid = (option >= rangeMinimum && option <= rangeMaximum);
                if (!optionIsValid) {System.out.println(INVALID_OPTION);}
            } catch (InputMismatchException e){
                System.out.println("You have not entered a number. " + INVALID_OPTION);
                scanner.nextLine();
            }
        }
        return option;
    }

    // Enter selected main menu option
    public static void openOptionFromMainMenu(int option) {
        String[] optionText = MAIN_MENU.split(System.lineSeparator());
        boolean customerExists = customer != null;

        switch (option) {
            // Find and reserve room
            case 1 -> {
                // System.out.println(optionText[0].substring(3));
                System.out.println(UNDERLINE);
                reserveRoom();
                System.out.println(UNDERLINE);
                returnDialog();
            }

            // Show reservations
            case 2 -> {
                // System.out.println(optionText[1].substring(3));
                System.out.println(UNDERLINE);
                if (!customerExists) {
                    System.out.println(ACCOUNT_REQUIRED);
                    System.out.println(UNDERLINE);
                    loginOrCreateAccount();
                }
                showReservations(customer.getEmail());
                System.out.println(UNDERLINE);
                returnDialog();
            }

            // Log in or create account
            case 3 -> {
                // System.out.println(optionText[2].substring(3));
                System.out.println(UNDERLINE);
                if (!customerExists) {
                    loginOrCreateAccount();
                } else {
                    boolean isLoggedIn = true;
                    while (isLoggedIn){
                        System.out.println("You are currently logged in. Sign out? Enter Y(es) / N(o)");
                        String response = scanner.next();
                        response = response.substring(0,1).toLowerCase();
                        System.out.println(response);

                        if (response.equals("y")) {
                            customer = HotelResource.getCustomer("");
                            isLoggedIn = false;
                            loginOrCreateAccount();
                        } else if (response.equals("n")) {
                            selectOption(null, 1, 5);
                        } else {
                            System.out.println("Please enter Y or N.");
                        }
                    }
                }
            }

            // Open Admin menu
            case 4 -> {
                // System.out.println(optionText[3].substring(3));
                System.out.println(UNDERLINE);
                try {
                    AdminMenu.runAdminApp();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // Close application
            case 5 -> exitApp();
        }
    }

    // if 1. then find and reserve room only if found rooms are more than 0
    public static Reservation reserveRoom() {
        boolean isReserved = false;

        while (!isReserved) {
            // fetch available rooms based on check in and checkout dates
            Collection<IRoom> availableRooms = findARoom();

            // Itemise available rooms
            Map<Integer, IRoom> itemisedRooms = new HashMap<>();
            int item = 1;
            for (IRoom room : availableRooms) {
                itemisedRooms.put(item, room);
                item++;
            }

            // number of rooms returned
            int numberOfRooms = itemisedRooms.size();

            if (numberOfRooms == 0){
                System.out.println("Unfortunately, there are no available rooms for your chosen date. Would you like to try alternative dates? " +
                        "\n Y(es) / N(o)");

                String userResponse = scanner.next().substring(0,1).toLowerCase();
                scanner.nextLine();

                switch (userResponse) {
                    case "y":
                        continue;
                    case "n":
                        // show main menu
                        openOptionFromMainMenu(selectOption(MAIN_MENU, 1, 5));
                        break;
                }
            }

            // chosen room;
            int option;

            // show all available rooms
            for (Map.Entry entry : itemisedRooms.entrySet()) {
                System.out.println(entry);
            }
            System.out.println(UNDERLINE);

            // menu showing all the room descriptions
            option = selectOption(ROOM_MENU, 1,3);

            // enable user to choose from the menu
            switch (option){
                case 1:
                    // account is required
                    if (customer == null) {
                        System.out.println(ACCOUNT_REQUIRED);
                        int accountOption = selectOption(ACCOUNT_MENU, 1,2);
                        switch (accountOption) {
                            case 1 -> logInToAccount();
                            case 2 -> createAccount();
                            default -> System.out.println("Welcome " + customer.getFirstName() + ". You are now logged in.");
                        }
                        continue;
                    }
                    break;    
                case 2:
                    // enter new dates
                    continue;
                case 3:
                    // show main menu
                    selectOption(MAIN_MENU, 1, 5);
                    break;
                case 0:
                    // exit application
                    exitApp();
            }

            boolean optionIsValid = false;
            // enter the room number of choice
            while (!optionIsValid){
                try {
                    System.out.println("Please choose a number from the list.");
                    option = scanner.nextInt();
                    optionIsValid = (option >= 1) && (option <= numberOfRooms);
                    if (!optionIsValid){
                        System.out.println(INVALID_OPTION);
                    }
                } catch (Exception e){
                    System.out.println("It seems like you did not enter a number. " + INVALID_OPTION);
                    scanner.nextLine();
                }
            }
            chosenRoom = itemisedRooms.get(option);
            System.out.println("Your have successfully reserved room " + chosenRoom.getRoomNumber() + ".");

            isReserved = true;
        }

        return HotelResource.bookARoom(customer.getEmail(), chosenRoom, checkInDate, checkOutDate);
    }

    private static Collection<IRoom> findARoom() {
        String checkIn;
        String checkOut;
        boolean checkInDateIsValid = false;
        boolean checkOutDateIsValid = false;

        do {
            //Find a room
            try {
                do {
                    System.out.println("Enter check in date in the format mm/dd/yyyy: ");
                    checkIn= scanner.next();
                    setCheckInDate(new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(checkIn));
                    checkInDateIsValid = (today.equals(checkInDate) || (today.before(checkInDate))
                            && oneYearFromToday.after(checkInDate));
                    if (!checkInDateIsValid) {
                        System.out.println("Please enter a valid date between " + today + " and " + oneYearFromToday);
                    }
                } while (!checkInDateIsValid);

                do {
                    System.out.println("Enter check out date in the format mm/dd/yyyy: ");
                    checkOut= scanner.next();
                    setCheckOutDate(new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(checkOut));
                    checkOutDateIsValid = checkInDate.before(checkOutDate) && oneYearFromToday.after(checkOutDate);
                    if (!checkOutDateIsValid) {
                        System.out.println("Please enter a valid date between "+ checkInDate + " and " + oneYearFromToday);
                    }
                } while (!checkOutDateIsValid);

            } catch (Exception ex) {
                System.out.println("You have entered an invalid date");
                scanner.nextLine();
            }

        } while (!(checkInDateIsValid && checkOutDateIsValid));

        // Found rooms
        return HotelResource.findARoom(checkInDate, checkOutDate);
    }

    // if 2. See all reservations- needs login
    private static void showReservations(String email) {
        if (customer == null) {
            loginOrCreateAccount();
        }
        Collection<Reservation> reservations = HotelResource.getCustomersReservations(email);
        if (reservations == null){
            System.out.println("You do not have any reservations currently!\n");
            openOptionFromMainMenu(selectOption(MAIN_MENU, 1, 5));
        } else {
            for (Reservation reservation: reservations) {
                System.out.println(reservation);
            }
        }
    }

    // if 3. login or create new account
    private static void createAccount() {
        String email = "";
        String firstName;
        String lastName;
        boolean emailIsValid = false;

        System.out.println("Please enter your first name: ");
        firstName = scanner.next();
        System.out.println("Please enter your last name: ");
        lastName = scanner.next();


        while (!emailIsValid){
            try {
                System.out.println("Please enter your email address: ");
                email = scanner.next();
                HotelResource.createACustomer(email, firstName, lastName);
                emailIsValid = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid email entered.");
            }
        }

        // set the session customer
        setCustomer(HotelResource.getCustomer(email));
        System.out.println("Account creation successful. Welcome "+ getCustomer().getFirstName()+"!\n");
    }

    private static void logInToAccount(){
        System.out.println("Please enter your email address: ");
        String email = scanner.next();
        if (HotelResource.getCustomer(email) == null){
            System.out.println("There is no account with this email address");
            createAccount();
        }
        setCustomer(HotelResource.getCustomer(email));
        System.out.println("Welcome back "+ customer.getFirstName()+".");
    }

    public static void loginOrCreateAccount(){
        int accountOption = selectOption(ACCOUNT_MENU, 1,3);
        switch (accountOption) {
            case 1 -> logInToAccount();
            case 2 -> createAccount();
            case 3 -> openOptionFromMainMenu(selectOption(MAIN_MENU, 1, 5));
        }
        scanner.nextLine();
        openOptionFromMainMenu(selectOption(MAIN_MENU, 1, 5));
    }

    // Exit app from anywhere
    public static void exitApp(){
        System.out.println("Thank you for your visit. Good bye.");
        scanner.close();
        System.exit(0);
    }

    public static void returnDialog() {
        System.out.println("What would you like to do? ");
        int option = selectOption(RETURN_MENU, 0, 1);
        switch (option){
            case 1 -> openOptionFromMainMenu(selectOption(MAIN_MENU, 1, 5));
            case 0 -> exitApp();
        }
    }

    public static void runMainApp() {
        System.out.println(UNDERLINE);
        System.out.println(WELCOME_MESSAGE);
        System.out.println(UNDERLINE);
        openOptionFromMainMenu(selectOption(MAIN_MENU, 1, 5));
        scanner.close();
    }
}
