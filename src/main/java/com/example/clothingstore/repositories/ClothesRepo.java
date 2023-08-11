package com.example.clothingstore.repositories;

import com.example.clothingstore.entities.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClothesRepo extends JpaRepository<Clothes, Long> {
    List<Clothes> findByGender(String gender);

    Clothes findByName(String name);

}
