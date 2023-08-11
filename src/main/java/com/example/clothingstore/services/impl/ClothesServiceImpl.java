package com.example.clothingstore.services.impl;

import com.example.clothingstore.entities.Clothes;
import com.example.clothingstore.repositories.ClothesRepo;
import com.example.clothingstore.services.ClothesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClothesServiceImpl implements ClothesService {

    private final ClothesRepo clothesRepo;
    @Override
    public List<Clothes> clothesByGender(String gender) {
        return clothesRepo.findByGender(gender);
    }

    @Override
    public Clothes clothesById(Long id) {
        return clothesRepo.getById(id);
    }

    @Override
    public Clothes getClothesById(Long id) {
        return clothesRepo.getById(id);
    }

    @Override
    public Clothes getClothesByName(String name) {
        return clothesRepo.findByName(name);
    }


}
