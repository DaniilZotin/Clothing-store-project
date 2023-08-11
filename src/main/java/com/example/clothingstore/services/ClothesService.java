package com.example.clothingstore.services;

import com.example.clothingstore.entities.Clothes;

import java.util.List;

public interface ClothesService {
    List<Clothes> clothesByGender(String gender);
    Clothes clothesById(Long id);

    Clothes getClothesById(Long id);

    Clothes getClothesByName(String name);

}
