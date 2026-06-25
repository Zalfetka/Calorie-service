package com.example.calorieCounter.repository;

import com.example.calorieCounter.dto.FoodMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FoodMongoRepo extends MongoRepository<FoodMongo, String> {
}
