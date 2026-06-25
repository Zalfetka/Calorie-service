package com.example.calorieCounter.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "daily_calories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyCaloriesMongo {
    @Id
    private String id;
    private Long userId;
    private LocalDate date;
    private Double consumedCalories;
    private Double consumedProteins;
    private Double consumedFats;
    private Double consumedCarbs;

}
