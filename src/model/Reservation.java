package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**Reservation model
 *
 * @author Jason Renek
 */
public class Reservation {     // Create Reservation class
    public Customer customer;  // Add variable Customer customer
    public Iroom room;         // Add variable Iroom room
    public Date checkInDate;   // Add variable Date checkInDate
    public Date checkOutDate;  // Add variable Date checkOutDate
    public String totalDays;
    public String totalNights;
    public String totalPrice;
    private static final DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    public Reservation(final Customer customer,
                       final Iroom room,
                       final Date checkInDate,
                       final Date checkOutDate,
                       final String totalDays,
                       final String totalNights,
                       final String totalPrice)
    {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalDays = totalDays;
        this.totalNights = totalNights;
        this.totalPrice = totalPrice;
    }

    public Iroom getRoom() {
        return this.room;
    }

    public Date getCheckInDate() {
        return this.checkInDate;
    }

    public Date getCheckOutDate() {
        return this.checkOutDate;
    }

    public String getTotalDays() { return this.totalDays; }

    public String getTotalNights() { return totalNights; }

    public String getTotalPrice() { return totalPrice; }

    @Override
    public String toString() {
        return this.customer.toString()
                + this.room.toString()
                + "\nCheckIn Date: " + formatter.format(this.checkInDate) + ","
                + "\nCheckOut Date: " + formatter.format(this.checkOutDate) +"," +
                "\n" + this.totalDays + " Days & " + this.totalNights + " Nights " + "Total Price $" + this.totalPrice;
    }
}
