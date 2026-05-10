package com.example.calorieCounter.repository;

import com.example.calorieCounter.entity.DailyCalories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CalorieRepo extends JpaRepository <DailyCalories, Long> {
    Optional<DailyCalories> findByUserIdAndDate (Long userId, LocalDate date);
}
