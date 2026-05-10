package com.example.calorieCounter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalorieDifferenceResponse {

    private LocalDate date;
    private Double consumedCalories;
    private Double consumedProteins;
    private Double consumedFats;
    private Double consumedCarbs;
}
