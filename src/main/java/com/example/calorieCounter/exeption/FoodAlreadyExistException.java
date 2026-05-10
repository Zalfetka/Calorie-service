package com.example.calorieCounter.exeption;

public class FoodAlreadyExistException extends RuntimeException {
    public FoodAlreadyExistException(String message) {
        super(message);
    }
}
