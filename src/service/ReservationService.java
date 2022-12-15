package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import java.util.*;



public class ReservationService {

    // Create singleton object

    private static final ReservationService SINGLETON = new ReservationService();

    // Keep collections of all rooms and reservations
    private static Map<String, IRoom> rooms = new HashMap<>();

    private final Map<String, Collection<Reservation>> reservations = new HashMap<>();


    // -----------------------------------------------------------------------

    private ReservationService() {
    }

    // return singleton of ReservationService class

    public static ReservationService getSingleton() {
        return SINGLETON;
    }



    public Map<String, IRoom> getRoomList() {
        return rooms;
    }

    // Methods

    // add room to collection
    public void addRoom(IRoom room) {
        rooms.put(room.getRoomNumber(), room);
    }

    // find room that matches one picked from menu
    public IRoom getARoom(String roomNo) {
        return rooms.get(roomNo);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {

        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);

        Collection<Reservation> reservations = getCustomerReservation(customer);

        // if no reservations, then create new Linked List

        if (Objects.isNull(reservations)) {
            reservations = new LinkedList<>();
        }

        reservations.add(reservation);
        this.reservations.put(customer.getEmail(), reservations);

        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate){

        // find rooms based on check-in and check-out dates
        // create collections for available rooms and rooms already in use
        Collection<IRoom> availableRooms = new LinkedList<>();
        Collection<IRoom> reservedRooms = findReservedRooms(checkInDate, checkOutDate);

        // add to available rooms list
        for (IRoom room : rooms.values()) {
            if (!reservedRooms.contains(room)) {
                availableRooms.add(room);
            }
        }
        // return list of available rooms
        return availableRooms;
    }

    //find the reserved rooms between check-in and check-out dates
    public Collection<IRoom> findReservedRooms(Date checkInDate, Date checkOutDate) {

        // create collections of reservations and reserved rooms
        Collection<Reservation> allReservations = getAllReservations();
        Collection<IRoom> reservedRooms = new LinkedList<>();

        // add rooms to reserved room list if:

        // 1) the check-in date is equal to or after the date of the reservation
        //    AND
        // 2) the check-out date is before or equal to the date of the reservation

        for(Reservation reservation : allReservations){
            if ((checkInDate.after(reservation.getCheckInDate()) || checkInDate.equals(reservation.getCheckInDate())) &&
                    checkOutDate.before(reservation.getCheckOutDate()) ||
                    checkOutDate.equals(reservation.getCheckOutDate())){
                reservedRooms.add(reservation.getRoom());
            }
        }

        // return a list of the reserved rooms
        return reservedRooms;
    }

    // get all reservations in collection

    private Collection<Reservation> getAllReservations() {

        Collection<Reservation> allReservations = new LinkedList<>();
        reservations.values().forEach(allReservations::addAll);
        return allReservations;
    }

    // get customer reservations

    public Collection<Reservation> getCustomerReservation(Customer customer) {
        return reservations.get(customer.getEmail());
    }


    // display all reservations
    public void printAllReservation() {
        Collection<Reservation> reservations = getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("Sorry, no reservations were found");
        } else {
            reservations.forEach(System.out::println);
        }
    }
}
