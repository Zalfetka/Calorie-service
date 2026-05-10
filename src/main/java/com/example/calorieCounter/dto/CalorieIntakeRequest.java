package com.example.calorieCounter.dto;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalorieIntakeRequest {
    private String foodName;
    @NotNull(message = "grams is required")
    @DecimalMin(value = "1.0", message = "Minimum is 1g")
    @DecimalMax(value = "2000.0", message = "Maximum is 2000g")
    private Double grams;
}
