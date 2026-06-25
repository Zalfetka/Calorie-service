package com.example.calorieCounter.globalException;

import com.example.calorieCounter.exeption.ExcelValidationException;
import com.example.calorieCounter.exeption.FoodAlreadyExistException;
import com.example.calorieCounter.exeption.FoodNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({FoodAlreadyExistException.class, IllegalArgumentException.class, FoodNotFoundException.class})
    public ResponseEntity<String> handleBadRequestExceptions(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }

    @ExceptionHandler(ExcelValidationException.class)
    public ResponseEntity<Map<String, Object>> handleExcelValidation(
            ExcelValidationException ex
    ) {

        return ResponseEntity.badRequest().body(
                Map.of(
                        "message", "Файл содержит ошибки",
                        "errors", Arrays.asList(ex.getMessage().split("\n"))
                )
        );
    }
}
