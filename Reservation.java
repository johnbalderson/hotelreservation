package model;

import java.util.Date;
import java.util.Objects;

public class Reservation {
    private Customer customer;
    private IRoom room;
    private Date checkInDate;
    private Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer() {return customer;}

    public IRoom getRoom() {return room;}

    public Date getCheckInDate() {return checkInDate;}

    public Date getCheckOutDate() {return checkOutDate;}

    @Override
    public String toString(){
        return "Reservation details" +
                "\nCustomer: " + customer.toString() +
                "\nRoom: " + room.toString() +
                "\nCheck-in: " + checkInDate +
                "\nCheck-out: " + checkOutDate;
    }


}
