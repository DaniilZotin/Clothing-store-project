package com.example.clothingstore.repositories;

import com.example.clothingstore.dao.ClothesDao;
import com.example.clothingstore.entities.Clothes;
import com.example.clothingstore.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Customer findById(long id);

    Customer findByUsername(String username);

    boolean existsByUsername(String username);
}
