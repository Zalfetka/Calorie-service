package com.example.calorieCounter.controller;

import com.example.calorieCounter.dto.FoodRequest;
import com.example.calorieCounter.dto.ImportResponse;
import com.example.calorieCounter.entity.Food;
import com.example.calorieCounter.service.FoodImportService;
import com.example.calorieCounter.service.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;
    private final FoodImportService foodImportService;

    @PostMapping("/add")
    public ResponseEntity<Void> addFood (@RequestBody  @Valid FoodRequest request) {
        foodService.addFood(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/get")
    public Food findFood(@RequestParam String foodName) {
        return foodService.getFood(foodName);
    }

    @PostMapping("/import")
    public ImportResponse importFood(@RequestParam("file") MultipartFile file) throws IOException {
        foodImportService.importExcel(file);
        return new ImportResponse("Данные успешно загружены");
    }
}
