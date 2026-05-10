package com.example.calorieCounter.dto;

import com.example.calorieCounter.annotation.ValidBjuSum;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@ValidBjuSum
public class FoodRequest {
    private String namefood;
    private Double protein;
    private Double carb;
    private Double fat;
}