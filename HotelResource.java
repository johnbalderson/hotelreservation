package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Calendar;
import java.util.Date;

public class HotelResource {

    // create singletons for HotelResource, CustomerService, and ReservationService
    private static final HotelResource SINGLETON = new HotelResource();

    private static final CustomerService customerService = CustomerService.getSingleton();
    private static final ReservationService reservationService = ReservationService.getSingleton();



    private HotelResource() {}

    public static HotelResource getSingleton() {
        return SINGLETON;
    }

    // link to modules in customerService and reservationService depending on use

    // get customer
    public static Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    // create nww customer
    public static void createACustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email, firstName, lastName);
    }

    public static IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    // book a room
    public static Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate){
        Customer customer = getCustomer(customerEmail);
        return reservationService.reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    // get customer reservations
    public static Collection<Reservation> getCustomerReservations(String customerEmail){
        Customer customer = getCustomer(customerEmail);
        return reservationService.getCustomerReservation(customer);
    }

    // find a room
    public static Collection<IRoom> findARoom(Date checkIn, Date checkOut)
    {
        return reservationService.findRooms(checkIn, checkOut);
    }








}

