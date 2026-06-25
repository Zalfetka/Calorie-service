package com.example.calorieCounter.service;

import com.example.calorieCounter.dto.FoodMongo;
import com.example.calorieCounter.dto.FoodRequest;
import com.example.calorieCounter.entity.Food;
import com.example.calorieCounter.exeption.FoodAlreadyExistException;
import com.example.calorieCounter.exeption.FoodNotFoundException;
import com.example.calorieCounter.repository.FoodMongoRepo;
import com.example.calorieCounter.repository.FoodRepo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
public class FoodService {

    private final FoodRepo foodRepo;
    private final FoodMongoRepo foodMongoRepo;

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

        FoodMongo mongoDoc = new FoodMongo();
        mongoDoc.setId(UUID.randomUUID().toString());
        mongoDoc.setNamefood(request.getNamefood());
        mongoDoc.setProtein(request.getProtein());
        mongoDoc.setCarb(request.getCarb());
        mongoDoc.setFat(request.getFat());
        foodMongoRepo.save(mongoDoc);
        foodRepo.save(foods);
    }

    public Food getFood(String foodName) {
        return foodRepo.findByNameFood(foodName)
                .orElseThrow(() -> new FoodNotFoundException("Food not found: " + foodName));
    }
}
