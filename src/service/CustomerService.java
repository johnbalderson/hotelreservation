package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {

    private static CustomerService INSTANCE = new CustomerService();

    public static CustomerService getInstance() {return INSTANCE;}

    private static final Map<String, Customer> customers = new HashMap<String, Customer>();

    public void addCustomer(String email, String firstName, String lastName) {
        Customer customer = new Customer(firstName, lastName, email);
        customers.put(email, customer);
    }

    public Customer getCustomer(String customerEmail) {return customers.get(customerEmail);}

    public Collection<Customer> getAllCustomers(){return customers.values();}
}
