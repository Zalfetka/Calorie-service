package com.example.calorieCounter.controller;

import com.example.calorieCounter.dto.FoodRequest;
import com.example.calorieCounter.service.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping("/add")
    public ResponseEntity<Void> addFood (@RequestBody  @Valid FoodRequest request) {
        foodService.addFood(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
