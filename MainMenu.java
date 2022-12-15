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

    static SimpleDateFormat standardDate = new SimpleDateFormat("MM/dd/yyyy");

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

    // getter and setter for check-in date

//    public static Date getCheckInDate() {
//        return checkInDate;
//    }

    public static void setCheckInDate(Date checkInDate) {
        MainMenu.checkInDate = checkInDate;
    }

    private static Date checkOutDate;

    // getter and setter for cheeck-out date

//    public static Date getCheckOutDate() {
//        return checkOutDate;
//    }

    public static void setCheckOutDate(Date checkOutDate) {
        MainMenu.checkOutDate = checkOutDate;
    }



    // Scanner
    private static Scanner scanner = new Scanner(System.in);

    // Customer in the session
    private static Customer customer;

    // getter and setter for customer

    public static Customer getCustomer() {
        return customer;
    }
    public static void setCustomer(Customer customer) {
        MainMenu.customer = customer;
    }



    // Current reservation
    static IRoom chosenRoom;

    // MAIN METHODS

    // Show main menu and option selections

    // 1) Find and reserve a room
    // 2) See my reservations
    // 3) Create an account (with name and e-mail address)
    // 4) Go to the ADMIN menu
    // 5) Exit
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

            // validate option entry
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

        boolean customerExists = customer != null;

        switch (option) {
            // option 1 - Find and reserve room
            case 1 -> {
                System.out.println(UNDERLINE);
                reserveRoom();
                System.out.println(UNDERLINE);
                returnDialog();
            }

            // option 2 - Show reservations
            case 2 -> {
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

            // option 3 - Log in or create account
            case 3 -> {
                System.out.println(UNDERLINE);
                if (!customerExists) {
                    loginOrCreateAccount();
                } else {
                    boolean isLoggedIn = true;
                    while (isLoggedIn){
                        System.out.println("You are currently logged in. Sign out? Enter Y(es) / N(o)");
                        String response = scanner.next();
                        response = response.substring(0,1).toLowerCase();
                        // System.out.println(response);

                        if (response.equals("y")) {
                            customer = HotelResource.getCustomer("");
                            isLoggedIn = false;
                            loginOrCreateAccount();
                        } else if (response.equals("n")) {
                            selectOption(MAIN_MENU, 0, 5);
                        } else {
                            System.out.println("Please enter Y or N.");
                        }
                    }
                }
            }

            // option 4 - go to ADMIN menu
            case 4 -> {
                System.out.println(UNDERLINE);
                try {
                    AdminMenu.runAdminApp();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // option 5 - Close application
            case 5 -> exitApp();
        }
    }

    // if 1. then find and reserve room only if found rooms are more than 0
    public static Reservation reserveRoom() {
        boolean isReserved = false;

        while (!isReserved) {
            // fetch available rooms based on check in and checkout dates
             Collection<IRoom> availableRooms = findARoom();
            //Collection<IRoom> availableRooms = HotelResource.findARoom(checkInDate, checkOutDate);



            // Count number of available rooms
            Map<Integer, IRoom> countRooms = new HashMap<>();
            int item = 1;
            for (IRoom room : availableRooms) {
                countRooms.put(item, room);
                item++;
            }

            // number of rooms returned
            int numberOfRooms = countRooms.size();

            // if there are no more available rooms, then suggest to user dates one week from today
            while (availableRooms.isEmpty()){

                Calendar calendar = Calendar.getInstance();

                // add 1 week to check-in date
                calendar.setTime(checkInDate);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                Date newCheckInDate = Date.from(calendar.toInstant());

                // add 1 week to check-out date
                calendar.setTime(checkOutDate);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                Date newCheckOutDate = Date.from(calendar.toInstant());

                // look for rooms using new date range of one week later
                availableRooms = HotelResource.findARoom(newCheckInDate, newCheckOutDate);

                // let user know there are no rooms available from the original date range
                //     but there are some available one week later, and show user those dates

                System.out.println("There are no more available rooms from " +
                        standardDate.format(checkInDate) + " tp " + standardDate.format(checkOutDate));
                System.out.println("We recommend you try booking rooms from "+
                            standardDate.format(newCheckInDate) + " to " + standardDate.format(newCheckOutDate));
            }

            // chosen room;
            int option;

            // show all available rooms
            for (Map.Entry entry : countRooms.entrySet()) {
                System.out.println(entry);
            }
            System.out.println(UNDERLINE);

            // menu showing all the room descriptions
            option = selectOption(ROOM_MENU, 0,3);

            // 1) Reserve a room
            // 2) Enter new dates
            // 3) Return to the main menu
            // 0) Exit

            // enable user to choose from the menu
            switch (option){
                case 1:
                    // if there's no customer, prompt user to create account
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
                    // enter new date range
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
            // enter the option number corresponding to the room number chosen
            // validate option number exists on room menu
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
            chosenRoom = countRooms.get(option);
            System.out.println("You have successfully reserved room " + chosenRoom.getRoomNumber() + ".");

            isReserved = true;
        }

        // book the room
        return HotelResource.bookARoom(customer.getEmail(), chosenRoom, checkInDate, checkOutDate);
    }

    // enter the check-in and check-out dates
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

        // Find a room using the check-in and check-out dates
        return HotelResource.findARoom(checkInDate, checkOutDate);
    }



    // option 2 - See all reservations- needs login
    private static void showReservations(String email) {
        if (customer == null) {
            loginOrCreateAccount();
        }
        Collection<Reservation> reservations = HotelResource.getCustomerReservations(email);
        if (reservations == null){
            System.out.println("You do not have any reservations currently!\n");
            openOptionFromMainMenu(selectOption(MAIN_MENU, 1, 5));
        } else {
            for (Reservation reservation: reservations) {
                System.out.println(reservation);
            }
        }
    }

    // option 3- Login to existing account or create a new account
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
                System.out.println("Please enter your e-mail address: ");
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

    // log into the account, and validate
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

    // login or create a new account
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

    // Exit app
    public static void exitApp(){
        System.out.println("Thank you for visiting. Goodbye and have a great day.");
        scanner.close();
        System.exit(0);
    }

    // ask user what to do next

    // 1) Return to the main menu
    // 2) Exit

    public static void returnDialog() {
        System.out.println("What would you like to do? ");
        int option = selectOption(RETURN_MENU, 0, 1);
        switch (option){
            case 1 -> openOptionFromMainMenu(selectOption(MAIN_MENU, 1, 5));
            case 0 -> exitApp();
        }
    }

    // run the main menu app
    public static void runMainApp() {
        System.out.println(UNDERLINE);
        System.out.println(WELCOME_MESSAGE);
        System.out.println(UNDERLINE);
        openOptionFromMainMenu(selectOption(MAIN_MENU, 1, 5));
        scanner.close();
    }
}
