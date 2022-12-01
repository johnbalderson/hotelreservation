package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;

public class ReservationService {

    private static ReservationService INSTANCE = new ReservationService();
    public static ReservationService getInstance() {return INSTANCE;}

    private static Map<String, IRoom> rooms = new HashMap<String, IRoom>();
    private static Map<Customer, List<Reservation>> reservations = new HashMap<Customer, List<Reservation>>();

    // Getters for databases
    public Map<String, IRoom> getRoomList() {
        return rooms;
    }

    public Map<Customer, List<Reservation>> getCustomerReservations() {
        return reservations;
    }

    // Methods
    public void addRoom(IRoom room){
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        return rooms.get(roomId);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {

        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);

        // if the customer already exists
        if (reservations.containsKey(customer)){
            // values is same as a list, so assign to list variable and add new reservation
            List<Reservation> customerReservations = ReservationService.reservations.get(customer);
            customerReservations.add(reservation);
        } else { // otherwise create new list of reservations
            List<Reservation> newCustomerReservation = new LinkedList<Reservation>();
            newCustomerReservation.add(reservation);
            reservations.put(customer, newCustomerReservation);
        }
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        // All rooms
        Collection<IRoom> roomList = rooms.values();

        // Reserved rooms
        List<Reservation> customerReservationList = new LinkedList<>();
        reservations.values().forEach(customerReservationList::addAll);

        // in reserved rooms check availability
        for (Reservation reservation : customerReservationList) {
            // Date conditions
            boolean sameDate = reservation.getCheckInDate().equals(checkInDate) ||
                    reservation.getCheckOutDate().equals(checkOutDate);
            boolean withinDateRange = reservation.getCheckInDate().after(checkInDate) &&
                    reservation.getCheckOutDate().before(checkOutDate);

            if (sameDate || withinDateRange) {
                // remove the room from room list if it is unavailable
                roomList.remove(reservation.getRoom());
            }
        }
        return roomList;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        return reservations.get(customer);
    }

    public void printAllReservation(){
        for (List<Reservation> reservation: reservations.values()) {
            System.out.println(reservation);
        }
    }
}
