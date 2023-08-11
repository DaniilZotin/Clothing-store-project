package com.example.clothingstore.services.impl;

import com.example.clothingstore.entities.Customer;
import com.example.clothingstore.repositories.CustomerRepo;
import com.example.clothingstore.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;


    @Override
    public Customer getCustomerById(long id) {
        return customerRepo.findById(id);
    }

    @Override
    public boolean authorizedCustomer(String usernameUnauthorizedCustomer, String passwordUnauthorizedCustomer) {
        try{
            return customerRepo.findByUsername(usernameUnauthorizedCustomer).getPassword().equals(passwordUnauthorizedCustomer);
        }catch (NullPointerException e){
            return false;
        }


    }

    @Override
    public long getIdByUsername(String username) {
        return customerRepo.findByUsername(username).getId();
    }

    @Override
    public void save(Customer customer) {
        customerRepo.save(customer);
    }
}
