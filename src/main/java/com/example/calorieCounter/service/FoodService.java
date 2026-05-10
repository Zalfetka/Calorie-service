package com.example.calorieCounter.service;

import com.example.calorieCounter.dto.FoodRequest;
import com.example.calorieCounter.entity.Food;
import com.example.calorieCounter.exeption.FoodAlreadyExistException;
import com.example.calorieCounter.repository.FoodRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class FoodService {

    private final FoodRepo foodRepo;

    public void addFood (FoodRequest request) {
        if (foodRepo.findByNameFood(request.getNamefood()).isPresent()) {
            throw new FoodAlreadyExistException("Food already exists");
        }

        Food foods = Food.builder()
                .nameFood(request.getNamefood())
                .protein(request.getProtein())
                .carb(request.getCarb())
                .fat(request.getFat())
                .build();

        foodRepo.save(foods);
    }
}
