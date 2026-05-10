package com.example.calorieCounter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "daily_calories")
@Builder
public class DailyCalories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long userId;
    @Column
    private Double dailyNorm;
    @Column(nullable = false)
    private Double consumedCalories;
    @Column
    private Double proteinNorm;
    @Column
    private Double consumedProteins;
    @Column
    private Double carbsNorm;
    @Column
    private Double consumedCarbs;
    @Column
    private Double fatNorm;
    @Column
    private Double consumedFats;
    @Column
    private Double carbsCalories;
    @Column
    private Double proteinCalories;
    @Column
    private Double fatCalories;
    @Column(nullable = false)
    private LocalDate createdAt;
    @Column
    private LocalDate date;
    @Column
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }

}
