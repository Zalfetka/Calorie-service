package com.example.calorieCounter.annotation;

import com.example.calorieCounter.dto.FoodRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BjuSumValidator implements ConstraintValidator<ValidBjuSum, FoodRequest> {

    @Override
    public boolean isValid(FoodRequest value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        double protein = value.getProtein() == null ? 0 : value.getProtein();
        double fat = value.getFat() == null ? 0 : value.getFat();
        double carb = value.getCarb() == null ? 0 : value.getCarb();
        double sum = protein + fat + carb;
        return sum <= 100;
    }
}
