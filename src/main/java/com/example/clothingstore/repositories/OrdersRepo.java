package com.example.clothingstore.repositories;

import com.example.clothingstore.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepo extends JpaRepository<Orders, Long> {

    List<Orders> findAllByCustomerId(long id);

    Orders findOrdersByCustomerId(long id);

    void deleteByCustomerId(long id);


}
