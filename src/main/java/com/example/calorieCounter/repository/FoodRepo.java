package com.example.calorieCounter.repository;

import com.example.calorieCounter.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FoodRepo extends JpaRepository<Food, Long> {
    Optional<Food> findByNameFood (String nameFood);
}
