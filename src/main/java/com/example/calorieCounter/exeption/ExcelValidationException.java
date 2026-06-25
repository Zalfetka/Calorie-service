package com.example.calorieCounter.exeption;

public class ExcelValidationException extends RuntimeException {
    public ExcelValidationException(String message) {
        super(message);
    }
}
