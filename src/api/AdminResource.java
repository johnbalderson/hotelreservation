package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {

    // create singleton for AdminResource
    private static final AdminResource SINGLETON = new AdminResource();

    private AdminResource() {}

    public static AdminResource getSingleton() {
        return SINGLETON;
    }

    // get singletons for Customer Service and Reservation Service
    private static final CustomerService customerService = CustomerService.getSingleton();
    private static final ReservationService reservationService = ReservationService.getSingleton();


    // add a room
    public static void addRoom(List<IRoom> rooms){
        for (IRoom room:rooms) {reservationService.addRoom(room);}
    }

    // get a collection of a list of reservations
    public static Collection<IRoom> getAllRooms(){
        return reservationService.getRoomList().values();
    }

    // get a collection of all customers
    public static Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // display all reservations
    public static void displayAllReservations() {
        reservationService.printAllReservation();
    }

}