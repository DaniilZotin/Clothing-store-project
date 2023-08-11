package com.example.clothingstore.services;

import com.example.clothingstore.entities.Customer;

import java.util.Optional;

public interface CustomerService {

    Customer getCustomerById(long id);

    boolean authorizedCustomer(String usernameUnauthorizedCustomer, String passwordUnauthorizedCustomer);

    long getIdByUsername(String username);

    void save(Customer customer);

}
