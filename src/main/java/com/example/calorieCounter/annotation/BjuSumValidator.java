package com.example.calorieCounter.annotation;

import com.example.calorieCounter.dto.FoodImportDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BjuSumValidator implements ConstraintValidator<ValidBjuSum, FoodImportDto> {

    @Override
    public boolean isValid(FoodImportDto value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        double protein = value.protein() == null ? 0 : value.protein();
        double fat = value.fat() == null ? 0 : value.fat();
        double carb = value.carb() == null ? 0 : value.carb();
        double sum = protein + fat + carb;
        return sum <= 100;
    }
}
