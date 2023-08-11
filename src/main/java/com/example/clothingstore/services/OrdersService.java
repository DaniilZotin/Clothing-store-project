package com.example.clothingstore.services;

import com.example.clothingstore.dao.BasketDao;
import com.example.clothingstore.entities.Clothes;
import com.example.clothingstore.entities.Orders;

import java.util.List;

public interface OrdersService {

    void save(Orders order);

    Orders createOrderEntityToMakeOrder(int quantity);

    List<Orders> getOrdersByIdCustomer(long id);

    Orders getOneOrderByIdCustomer(long id);

    List<BasketDao> getBasketFromTwoList(List<Orders> orders, List<Clothes> clothes);

    void deleteByCustomerId(Long id);
}
