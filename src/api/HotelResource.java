package api;

import model.Customer;
import model.Iroom;
import service.CustomerService;
import service.ReservationService;
import model.Reservation;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**Hotel Resource api
 *
 * @author Jason Renek
 */
public class HotelResource {

    private static HotelResource hotelResource;
    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    private HotelResource() {}

    public static synchronized HotelResource getInstance() {        // static reference
        if(hotelResource == null) {
            hotelResource = new HotelResource();
        }
        return hotelResource;
    }


    public Customer getCustomer(String email)                       // Customer getCustomer
    {
        CustomerService service = CustomerService.getInstance();
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email,                       // createACustomer
                                String firstName,
                                String lastName) {
        customerService.addCustomer(email,
                firstName,
                lastName);
    }

    public Iroom getRoom(String roomNumber) {                       // Iroom getRoom
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail,              // bookARoom
                                 Iroom room,
                                 Date checkInDate,
                                 Date checkOutDate,
                                 String totalDays,
                                 String totalNights,
                                 String totalPrice) {
        return reservationService.reserveARoom(getCustomer(customerEmail),
                room,
                checkInDate,
                checkOutDate,
                totalDays,
                totalNights,
                totalPrice);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {   // Collection
        Customer customer = getCustomer(customerEmail);                                   // getCustomerReservations

        if (customer == null) {
            return Collections.emptyList();
        }
        else {
            return reservationService.getCustomersReservation(getCustomer(customerEmail));
        }
    }

    public Collection<Iroom> findARoom(Date checkIn,            // Collection findARoom
                                       Date checkOut) {
        return reservationService.findRooms(checkIn,
                checkOut);
    }

    public Collection<Iroom> findAltRooms(final Date checkIn,
                                          final Date checkOut) {
        return reservationService.findAltRooms(checkIn,
                checkOut);
    }

    public Date addAltDays(final Date date) {
        return reservationService.addAltDays(date);
    }
}
