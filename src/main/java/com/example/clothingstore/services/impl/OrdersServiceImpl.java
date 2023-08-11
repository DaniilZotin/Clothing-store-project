package com.example.clothingstore.services.impl;

import com.example.clothingstore.dao.BasketDao;
import com.example.clothingstore.entities.Clothes;
import com.example.clothingstore.entities.Orders;
import com.example.clothingstore.repositories.ClothesRepo;
import com.example.clothingstore.repositories.OrdersRepo;
import com.example.clothingstore.services.OrdersService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.clothingstore.controllers.CustomerController.CURRENT_CLOTHES_ID;
import static com.example.clothingstore.controllers.CustomerController.CURRENT_CUSTOMER_ID;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepo ordersRepo;
    private final ClothesRepo clothesRepo;

    @Override
    public void save(Orders order) {
        ordersRepo.save(order);
    }

    @Override
    public Orders createOrderEntityToMakeOrder(int quantity) {
        Orders orderWithStuff = new Orders();
        System.out.println(CURRENT_CUSTOMER_ID);
        orderWithStuff.setCustomerId(CURRENT_CUSTOMER_ID);
        orderWithStuff.setClothesId(CURRENT_CLOTHES_ID);
        orderWithStuff.setQuantity(quantity);
        orderWithStuff.setPrice(clothesRepo.findById(CURRENT_CLOTHES_ID).get().getPrice() * quantity);
        return orderWithStuff;
    }

    @Override
    public List<Orders> getOrdersByIdCustomer(long id) {
        return ordersRepo.findAllByCustomerId(id);
    }

    @Override
    public Orders getOneOrderByIdCustomer(long id) {
        return ordersRepo.findOrdersByCustomerId(id);
    }


    //Combine two list in one
    @Override
    public List<BasketDao>  getBasketFromTwoList(List<Orders> ordersList, List<Clothes> clothesList) {
        List<BasketDao> basket = new ArrayList<>();
        for (Orders order : ordersList) {
            BasketDao basketDao = new BasketDao();
            basketDao.setQuantity(order.getQuantity());
            basketDao.setPrice(order.getPrice());

            for (Clothes clothes : clothesList) {
                if (order.getClothesId() == clothes.getId()) {
                    basketDao.setName(clothes.getName());
                    basketDao.setImage(clothes.getImage());
                    basketDao.setPriceByOne(clothes.getPrice());
                    break;
                }
            }

            basket.add(basketDao);
        }
        return basket;
    }

    @Transactional
    @Override
    public void deleteByCustomerId(Long id) {
        ordersRepo.deleteByCustomerId(id);
    }
}
