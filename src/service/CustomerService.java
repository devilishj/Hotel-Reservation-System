package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public final class CustomerService {
    private static CustomerService customerService;
    private final Map<String, Customer> customers = new HashMap<>();

    private CustomerService() {}
    // default method
    static synchronized CustomerService getInstance() {   // looking ahead to Advanced java discovered
        if(customerService == null) {                            // Singleton pattern after hearing about it
            customerService = new CustomerService();             // from session leads
        }
        return customerService;
    }

    public void addCustomer(String email, String firstName, String lastName) {  // addCustomer
        customers.put(email, new Customer(firstName, lastName, email));
    }

    public Customer getCustomer(String customerEmail) {
        return customers.get(customerEmail);
    }  // getCustomer

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }  // public Collection
}
