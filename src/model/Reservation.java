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

    public void setCustomer(Customer customer) {this.customer = customer;}

    public IRoom getRoom() {return room;}

    public void setRoom(IRoom room) {this.room = room;}

    public Date getCheckInDate() {return checkInDate;}

    public void setCheckInDate(Date checkInDate) {this.checkInDate = checkInDate;}

    public Date getCheckOutDate() {return checkOutDate;}

    public void setCheckOutDate(Date checkOutDate) {this.checkOutDate = checkOutDate;}

    @Override
    public String toString(){
        return "Reservation details" +
                "\nCustomer: " + customer.toString() +
                "\nRoom: " + room.toString() +
                "\nCheck-in: " + checkInDate +
                "\nCheck-out: " + checkOutDate;
    }

    @Override
    public boolean equals (Object o){
        // compare with itself
        if (this == o) {
            return true;
        }
        // compare if o is an instance
        if (!(o instanceof Reservation)) {
            return false;
        }

        /*
        Cast o to an instance and
        compare elements of this to o
         */
        Reservation r = (Reservation) o;
        return Objects.equals(r.getCheckInDate(), this.getCheckInDate()) &&
                Objects.equals(r.getCheckOutDate(), this.getCheckOutDate()) &&
                Objects.equals(r.getCustomer(), this.getCustomer()) &&
                Objects.equals(r.getRoom(), this.getRoom());

    }

    @Override
    public int hashCode() {
        return Objects.hash(getCheckInDate(), getCheckOutDate(), getCustomer(), getRoom());
    }
}
