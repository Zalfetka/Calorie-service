package com.example.calorieCounter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "food_calories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodMongo {
    private String id;
    private LocalDate date;
    private String namefood;
    private Double protein;
    private Double carb;
    private Double fat;
}
