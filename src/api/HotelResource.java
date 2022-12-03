package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Calendar;
import java.util.Date;

public class HotelResource {

    private static final CustomerService customerService = CustomerService.getSingleton();
    private static final ReservationService reservationService = ReservationService.getSingleton();

    public static Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public static void createACustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email, firstName, lastName);
    }

    public static IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    public static Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate){
        Customer customer = getCustomer(customerEmail);
        return reservationService.reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    public static Collection<Reservation> getCustomersReservations(String customerEmail){
        Customer customer = getCustomer(customerEmail);
        return reservationService.getCustomersReservation(customer);
    }

    public static Collection<IRoom> findARoom(Date checkIn, Date checkOut){
        Collection<IRoom> availableRooms = reservationService.findRooms(checkIn, checkOut);
        //Search for alternative rooms
        if (availableRooms.size() == 0) {
            System.out.println("No room found for given dates. Searching for recommended rooms...");
            Collection<IRoom> recommendedRooms;
            Calendar calendar = Calendar.getInstance();

            // add 1 week to checkindate
            calendar.setTime(checkIn);
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
            Date newCheckIn = Date.from(calendar.toInstant());

            // add 1 week to chekoutdate
            calendar.setTime(checkOut);
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
            Date newCheckOut = Date.from(calendar.toInstant());
            // find rooms with new dates
            // update value of available rooms
            recommendedRooms = reservationService.findRooms(newCheckIn, newCheckOut);
            // add a print statement to show that these are recommended rooms
            if (recommendedRooms.size() != 0){
                System.out.println("Showing recommended rooms from: "+ newCheckIn +" to"+ newCheckOut);
                return recommendedRooms;
            }
        }
        return availableRooms;
    }
}
