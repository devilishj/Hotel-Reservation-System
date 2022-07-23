package api;

import model.Customer;
import model.Iroom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

/**Admin Resource api
 *
 * @author Jason Renek
 */
public final class AdminResource {
    private static AdminResource adminResource;
    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    private AdminResource() {}

    public static synchronized AdminResource getInstance() {        // Singleton pattern
        if(adminResource == null) {
            adminResource = new AdminResource();
        }
        return adminResource;
    }

    public Customer getCustomer(String email) {      // Customer getCustomer
        return customerService.getCustomer(email);
    }
    public void addRoom(List<Iroom> rooms) {         // addRoom
        //rooms.forEach(room -> reservationService.addRoom(room));  // started with this but prompted to
        rooms.forEach(reservationService::addRoom);                 // replace lambda method with this
    }
    public Collection<Iroom> getAllRooms() {         // Iroom getAllRooms
        return reservationService.getAllRooms();
    }
    public Collection<Customer> getAllCustomers() {  // getAllCustomers
        return customerService.getAllCustomers();
    }
    public void displayAllReservations() {           // displayAllReservations
        reservationService.printAllReservation();
    }
}
