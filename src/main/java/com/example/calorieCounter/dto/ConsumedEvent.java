package com.example.calorieCounter.dto;

import com.example.calorieCounter.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumedEvent {
    private String eventId;
    private LocalDateTime timestamp;
    private Long userId;
    private Double calories;
    private Double proteins;
    private Double fats;
    private Double carbs;
    private Integer version;
    private Status status;
}
