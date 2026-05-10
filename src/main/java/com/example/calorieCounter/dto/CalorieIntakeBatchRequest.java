package com.example.calorieCounter.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalorieIntakeBatchRequest {
    @NotEmpty(message = "Food list cannot be empty")
    private List<@Valid CalorieIntakeRequest> items;
}
