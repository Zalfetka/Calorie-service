package com.example.calorieCounter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FoodRequest {
    private String namefood;
    private Double protein;
    private Double carb;
    private Double fat;
}