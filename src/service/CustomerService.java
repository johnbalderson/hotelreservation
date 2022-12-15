package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {

    // create singleton object
    private static final CustomerService SINGLETON = new CustomerService();

    private CustomerService() {}

    public static CustomerService getSingleton() {
        return SINGLETON;
    }

    private static final Map<String, Customer> customers = new HashMap<>();

    public void addCustomer(String email, String firstName, String lastName) {
        Customer customer = new Customer(firstName, lastName, email);
        customers.put(email, customer);
    }

    public Customer getCustomer(String customerEmail)
     {
         return customers.get(customerEmail);
     }

    public Collection<Customer> getAllCustomers()
    {
        return customers.values();
    }
}

