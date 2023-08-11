package com.example.clothingstore.dao;

import com.example.clothingstore.entities.Clothes;
import com.example.clothingstore.entities.Customer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class CustomerDao {

    String name;
    String surname;
    String username;
    String password;
    LocalDate dateRegistration;

    List<ClothesDao> clothes;

    public static CustomerDao customerToCustomerDao(Customer customer){
        CustomerDao customerDao = new CustomerDao();
        customerDao.setName(customer.getName());
        customerDao.setSurname(customer.getSurname());
        customerDao.setPassword(customer.getPassword());
        customerDao.setDateRegistration(customer.getDateRegistration());
        customerDao.setUsername(customer.getUsername());
        customerDao.setClothes(customer.getClothes().stream().map(ClothesDao::clothesToClothesDao).collect(Collectors.toList()));
        return customerDao;
    }

}
