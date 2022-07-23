package service;

import model.Customer;
import model.Iroom;
import model.Reservation;

import java.util.*;
import java.util.stream.Collectors;

/**Reservation Service
 *
 * @author Jason Renek
 */
public final class ReservationService {
    private static ReservationService reservationService;
    private static final int addAltDays = 7;
    private final Map<String, Collection<Reservation>> reservations = new HashMap<>();
    private final Map<String, Iroom> rooms = new HashMap<>();

    ReservationService() {}
     static synchronized ReservationService getInstance() {  // default method
        if(reservationService == null) {
            reservationService = new ReservationService();
        }
        return reservationService;
    }

    public void addRoom(Iroom room){
        rooms.put(room.getRoomNumber(), room);  // addRoom
    }

    public Iroom getARoom(String roomNumber) {
        return rooms.get(roomNumber);           // getARoom
    }

    public Collection<Iroom> getAllRooms() {    // Collection getAllRooms
        return rooms.values();
    }

    public Reservation reserveARoom(Customer customer,
                                    Iroom room,
                                    Date checkInDate,
                                    Date checkOutDate,
                                    String totalDays,
                                    String totalNights,
                                    String totalPrice) {
        Reservation reservation = new Reservation(customer,
                room,
                checkInDate,
                checkOutDate,
                totalDays,
                totalNights,
                totalPrice);  // reserveARoom

        Collection<Reservation> customerReservations = getCustomersReservation(customer);

        if(customerReservations == null) {
            customerReservations = new LinkedList<>();
        }

        customerReservations.add(reservation);
        reservations.put(customer.getEmail(), customerReservations);

        return reservation;
    }



    public Collection<Iroom> findRooms(Date checkInDate,
                                       Date checkOutDate) {   // Collection findRooms
        return findAvailableRooms(checkInDate,
                checkOutDate);
    }

    public Collection<Iroom> findAltRooms(final Date checkInDate,       // Collection
                                          final Date checkOutDate) {    // findAlternativeRooms
        return findAvailableRooms(addAltDays(checkInDate), addAltDays(checkOutDate));
    }

    private Collection<Iroom> findAvailableRooms(Date checkInDate,              // findAvailableRooms
                                                 Date checkOutDate) {

        final Collection<Reservation> allReservations = getAllReservations();
        final Collection<Iroom> notAvailableRooms = new LinkedList<>();

        for (Reservation reservation : allReservations) {
            if (reservationOverlaps(reservation,
                    checkInDate,
                    checkOutDate)) {
                notAvailableRooms.add(reservation.getRoom());
            }
        }

        return rooms.values().stream().filter(room -> notAvailableRooms.stream()
                        .noneMatch(notAvailableRoom -> notAvailableRoom.equals(room)))
                .collect(Collectors.toList());
    }

    public Date addAltDays(final Date date) {          // Add 7 alternative days
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, addAltDays);

        return calendar.getTime();
    }

    private boolean reservationOverlaps(final Reservation reservation,      // check for reservation overlap
                                        final Date checkInDate,
                                        final Date checkOutDate){
        return checkInDate.before(reservation.getCheckOutDate())
                && checkOutDate.after(reservation.getCheckInDate());
    }

    public Collection<Reservation> getCustomersReservation(final Customer customer) {  // getCustomerReservation
        return reservations.get(customer.getEmail());
    }

    public void printAllReservation() {
        final Collection<Reservation> reservations = getAllReservations();      // printAllReservations

        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation + "\n");
            }
        }
    }

    private Collection<Reservation> getAllReservations() {
        final Collection<Reservation> allReservations = new LinkedList<>();     // getAllReservations

        for(Collection<Reservation> reservations : reservations.values()) {
            allReservations.addAll(reservations);
        }

        return allReservations;
    }
}
