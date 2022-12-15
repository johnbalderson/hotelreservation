package menu;

import api.AdminResource;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.*;

public class AdminMenu {

    private static Collection<Customer> allCustomers = AdminResource.getAllCustomers();

    private static final String WELCOME_MESSAGE = "" +
            "Welcome to the Udacity Hotel Reservation Administration Menu";
    private static final String UNDERLINE = "-------------------------------";
    private static final String ADMIN_MENU = """
            1. See all customers
            2. See all rooms
            3. See all reservations
            4. Add a room
            5. Back to the main menu
            """;
    private static final String INVALID_OPTION = "Please choose a valid number from the list.";

    private static final String ROOM_TYPE_MENU = """
            Select the type of the room. 
            Enter:
            1 for Single Room
            2 for Double Room
            """;

    private static final String RETURN_MENU = """
            1. Return to the admin menu
            2. Return to the main menu
            """;

    // use scanner to parse entries in menu
    private static Scanner scanner = new Scanner(System.in);
    // while isRunning
    // Show the admin menu list 1 - 5

    // Show main menu and option selection
    public static void showMenu(String MENU) {
        Optional<String> input = Optional.ofNullable(MENU);
        String menu = input.isPresent() ? input.get() : ADMIN_MENU;
        System.out.println(menu);
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
                if (!optionIsValid) {
                    System.out.println(INVALID_OPTION);
                }
            } catch (InputMismatchException e) {
                System.out.println("You have not entered a number. " + INVALID_OPTION);
                scanner.nextLine();
            }
        }
        return option;
    }

    public static void openOptionsFromAdminMenu(int option) {

        // 1) See all customers
        // 2) See all rooms
        // 3) See all reservations
        // 4) Add a room
        // 5) Return to main menu

        switch (option) {

            // option 1 - See all customers (AdminResource.getAllCustomers)

            case 1 -> {

                System.out.println(UNDERLINE);
                showAllCustomers();
                System.out.println(UNDERLINE);
                returnDialog();
            }

            // option 2 - See all rooms (AdminResource.getAllRooms)

            case 2 -> {
                // System.out.println(optionText[1].substring(3));
                System.out.println(UNDERLINE);
                showAllRooms();
                System.out.println(UNDERLINE);
                returnDialog();
            }

            // option 3 - See all reservations (AdminResource.displayAllReservations)

            case 3 -> {
                // System.out.println(optionText[2].substring(3));
                System.out.println(UNDERLINE);
                showAllReservations();
                System.out.println(UNDERLINE);
                returnDialog();
            }

            // option 4 - Add a Room (Use AdminResource.addroom)

            case 4 -> {
                // System.out.println(optionText[3].substring(3));
                System.out.println(UNDERLINE);
                addARoom();
                System.out.println(UNDERLINE);
                returnDialog();
            }

            // option 5 - return to main menu (run MainMenu.runMainApp)
            //
            case 5 -> {
                try {
                    MainMenu.runMainApp();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void addARoom() {

        List<IRoom> rooms = new LinkedList<IRoom>();

        // while loop
        boolean isFinished = false;
        while (!isFinished) {
            // Declare room
            IRoom room;
            // open menu

            // validate room number

            int roomNumber = 0;
            boolean hasRoomNumber = false;
            while (!hasRoomNumber) {
                // enter room number
                System.out.println("Enter the room number: ");

                try {
                    roomNumber = scanner.nextInt();
                    hasRoomNumber = roomNumber > 0;
                } catch (InputMismatchException e) {
                    System.out.println("Room number must be numeric.");
                    scanner.nextLine();
                }
            }

            // validate price


                Double price = 0.0;
                boolean hasPrice = false;
                while (!hasPrice) {
                    // enter price
                    System.out.println("Enter the price of the room: ");

                    try {
                        price = scanner.nextDouble();
                        hasPrice = price > 0;
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter a valid numeric price in the format x.xx .");
                        scanner.nextLine();
                    }
                }


                // enter room type
                RoomType roomType = null;
                boolean hasRoomType = false;
                while (!hasRoomType) {
                    // only 1 0r 2
                    int type = selectOption(ROOM_TYPE_MENU, 1, 2);
                    hasRoomType = (type == 1 || type == 2);
                    if (hasRoomType) {
                        switch (type) {
                            case 1 -> roomType = RoomType.SINGLE;
                            case 2 -> roomType = RoomType.DOUBLE;
                        }
                    } else {
                        System.out.println(INVALID_OPTION);
                    }
                }

                // add a room
                String roomNumberString = String.valueOf(roomNumber);
                room = new Room(roomNumberString, price, roomType);
                rooms.add(room);
                System.out.println("Room " + roomNumberString + " added.\n" +
                        UNDERLINE);

                // ask if they would like to add another room
                System.out.println("Would you like to enter another room? Enter Y(es) / N(o)");
                String response = scanner.next();
                response = response.substring(0, 1).toLowerCase();

                // exit loop if no
                switch (response) {
                    case "y":
                        scanner.nextLine();
                        continue;
                    case "n":
                        isFinished = true;
                }
            }

            //Add all rooms
            AdminResource.addRoom(rooms);
            openOptionsFromAdminMenu(selectOption(ADMIN_MENU, 1, 5));
        }

        // show all reservations
        private static void showAllReservations () {
            AdminResource.displayAllReservations();
        }

        // show all the rooms
        private static void showAllRooms () {
            String roomHeader = String.format("%-12s", "Room Number");
            String typeHeader = String.format("%-12s", "Type");
            String priceHeader = String.format("%-12s", "Price");

            System.out.println(roomHeader + "|" + typeHeader + "|" + priceHeader);
            Collection<IRoom> allRooms = AdminResource.getAllRooms();
            for (IRoom room : allRooms) {
                System.out.println(room);
            }
        }

        // show all the customers
        public static void showAllCustomers () {
            System.out.println(String.format("%-12s", "First Name") +
                    "|" + String.format("%-12s", "Surname") +
                    "|" + String.format("%-12s", "Email Address"));
            for (Customer customer : allCustomers) {
                System.out.println(customer);
            }
        }

        // ask user what they'd like to do next

        // 1) return to admin menu
        // 2) return to main menu

        public static void returnDialog () {
            System.out.println("What would you like to do? ");
            int option = selectOption(RETURN_MENU, 1, 2);
            switch (option) {
                case 1 -> openOptionsFromAdminMenu(selectOption(ADMIN_MENU, 1, 5));
                case 2 -> {
                    try {
                        MainMenu.runMainApp();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        // run the Admin menu
        public static void runAdminApp () throws Exception {
            System.out.println(UNDERLINE);
            System.out.println(WELCOME_MESSAGE);
            System.out.println(UNDERLINE);
            int option = selectOption(ADMIN_MENU, 1, 5);
            openOptionsFromAdminMenu(option);
            scanner.close();
        }

    }

