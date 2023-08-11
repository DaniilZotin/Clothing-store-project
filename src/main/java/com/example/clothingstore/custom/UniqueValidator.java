package com.example.clothingstore.custom;

import com.example.clothingstore.entities.Customer;
import com.example.clothingstore.repositories.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class UniqueValidator implements ConstraintValidator<Unique, String> {

    @Autowired
    private CustomerRepo customerRepo; // Замініть `YourRepository` на ваш репозиторій

    @Override
    public void initialize(Unique constraintAnnotation) {
        // Метод ініціалізації, якщо потрібно
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Логіка перевірки унікальності
        try{
            return value != null && !customerRepo.existsByUsername(value); // Замініть `fieldName` та `existsByFieldName` на ваші поля та методи перевірки
        } catch (NullPointerException e){
            System.out.println("NullPointer I have to fix it ");
            return true;
        }
    }
}
