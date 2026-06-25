package com.example.calorieCounter.controller;

import com.example.calorieCounter.dto.*;
import com.example.calorieCounter.entity.DailyCalories;
import com.example.calorieCounter.service.CalorieTrackingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/calories")
@RequiredArgsConstructor
public class CalorieController {

    private final CalorieTrackingService calorieTrackingService;

    @PostMapping("/intake/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public CalorieDifferenceResponse calorieDifferenceResponse (@PathVariable Long userId, @Valid @RequestBody CalorieIntakeBatchRequest request) throws JsonProcessingException {
        return calorieTrackingService.addCaloriesAndGetDifference(userId, request);
    }

    @GetMapping("/get")
    public DailyCalories getDailyCalories(Long userId, LocalDate date) {
        return calorieTrackingService.getDailyCalories(userId, date);
    }
}
