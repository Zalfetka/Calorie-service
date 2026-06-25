package com.example.calorieCounter.dto;

import com.example.calorieCounter.annotation.ValidBjuSum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@ValidBjuSum
public record FoodImportDto(

        @NotBlank(message = "Название продукта обязательно")
        String nameFood,
        @PositiveOrZero(message = "Белки не могут быть отрицательными")
        Double protein,
        @PositiveOrZero(message = "Углеводы не могут быть отрицательными")
        Double carb,
        @PositiveOrZero(message = "Жиры не могут быть отрицательными")
        Double fat
) {
}
