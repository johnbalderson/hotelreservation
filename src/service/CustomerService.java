package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {

    private static final CustomerService SINGLETON = new CustomerService();

    private final Map<String, Customer> customers = new HashMap<>();

    private CustomerService() {}
    public static CustomerService getSingleton()
          {return SINGLETON;}



    public void addCustomer(String email, String firstName, String lastName) {
        Customer customer = new Customer(firstName, lastName, email);
        customers.put(email, customer);
    }

    public Customer getCustomer(String customerEmail) {return customers.get(customerEmail);}

    public Collection<Customer> getAllCustomers(){return customers.values();}
}
