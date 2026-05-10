package com.example.calorieCounter.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BjuSumValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBjuSum {
    String message() default "Sum of protein, fat and carb must not exceed 100";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

