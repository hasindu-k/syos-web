// src/service/CustomerService.java
package service;

import dao.CustomerDao;
import model.Customer;

import java.util.List;

/**
 * Service class for Customer operations.
 */
public class CustomerService {
    private CustomerDao customerDao;

    public CustomerService() {
        this.customerDao = new CustomerDao();
    }

    /**
     * Adds a new customer.
     * 
     * @param name  Customer name.
     * @param email Customer email.
     * @param phone Customer phone.
     * @return Customer ID if successful, else -1.
     */
    public int addCustomer(String name, String email, String phone) {
        Customer customer = new Customer(name, email, phone);
        return customerDao.addCustomer(customer);
    }

    /**
     * Retrieves all customers.
     * 
     * @return List of customers.
     */
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }

    /**
     * Retrieves a customer by their ID.
     * 
     * @param id Customer ID.
     * @return Customer object if found, else null.
     */
    public Customer getCustomerById(Integer id) {
        return customerDao.getCustomerById(id);
    }

    public void registerCustomer(String name, String email, String phone) {
        Customer customer = new Customer(name, email, phone);

        if (!isValidEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (emailExists(customer.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        customerDao.addCustomer(customer);
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    }

    private boolean emailExists(String email) {
        return customerDao.emailExists(email);
    }

    public Integer getCustomerIdByUsername(String username) {
        return customerDao.getCustomerIdByUsername(username);
    }
}
