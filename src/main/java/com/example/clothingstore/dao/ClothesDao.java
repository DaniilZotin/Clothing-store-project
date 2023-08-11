package com.example.clothingstore.dao;

import com.example.clothingstore.entities.Clothes;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClothesDao {


    String name;
    Double price;
    String image;
    String gender;
//    Customer customer;

    public static ClothesDao clothesToClothesDao(Clothes clothes){
        ClothesDao clothesDao = new ClothesDao();
        clothesDao.setName(clothes.getName());
        clothesDao.setGender(clothes.getGender());
//        clothesDao.setCustomer(clothes.getCustomer());
        clothesDao.setPrice(clothes.getPrice());
        clothesDao.setImage(clothes.getImage());
        return clothesDao;
    }
}
