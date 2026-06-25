package com.example.calorieCounter.repository;

import com.example.calorieCounter.dto.DailyCaloriesMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CalorieMongoRepo extends MongoRepository<DailyCaloriesMongo, String> {
}
