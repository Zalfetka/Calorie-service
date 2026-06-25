package com.example.calorieCounter.service;

import com.example.calorieCounter.dto.*;
import com.example.calorieCounter.entity.DailyCalories;
import com.example.calorieCounter.entity.Food;
import com.example.calorieCounter.entity.KafkaMessageLogEntity;
import com.example.calorieCounter.enums.Status;
import com.example.calorieCounter.exeption.FoodNotFoundException;
import com.example.calorieCounter.exeption.UserNotFoundException;
import com.example.calorieCounter.kafka.UserProducer;
import com.example.calorieCounter.repository.CalorieRepo;
import com.example.calorieCounter.repository.FoodRepo;
import com.example.calorieCounter.repository.KafkaMessageLogRepository;
import com.example.calorieCounter.repository.CalorieMongoRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CalorieTrackingService {

    private final CalorieRepo calorieRepo;
    private final UserCache userCache;
    private final FoodRepo foodRepo;
    private final UserProducer userProducer;
    private final KafkaMessageLogRepository kafkaMessageLogRepository;
    private final ObjectMapper objectMapper;
    private final CalorieMongoRepo mongoRepo;

    @Transactional
    public CalorieDifferenceResponse addCaloriesAndGetDifference(Long userId, CalorieIntakeBatchRequest request) throws JsonProcessingException {
        UserDTO user = userCache.get(userId);

        if (user == null) {
            throw new RuntimeException("User not found in cache (Kafka not received yet)");
        }

        DailyCalories dailyCalories = getOrCreateDailyRecord(userId, user);

        for (CalorieIntakeRequest item : request.getItems()) {
            Food product = foodRepo.findByNameFood(item.getFoodName())
                    .orElseThrow(() -> new FoodNotFoundException("Product not found: " + item.getFoodName()));
            double grams = Optional.ofNullable(item.getGrams()).orElse(0.0);
            double factor = grams / 100.0;
            double proteins = product.getProtein() * factor;
            double fats = product.getFat() * factor;
            double carbs = product.getCarb() * factor;
            double calories = proteins * 4 + fats * 9 + carbs * 4;
            dailyCalories.setConsumedCalories(dailyCalories.getConsumedCalories() + calories);
            dailyCalories.setConsumedProteins(dailyCalories.getConsumedProteins() + proteins);
            dailyCalories.setConsumedFats(dailyCalories.getConsumedFats() + fats);
            dailyCalories.setConsumedCarbs(dailyCalories.getConsumedCarbs() + carbs);
        }
        calorieRepo.save(dailyCalories);
        DailyCaloriesMongo mongoDoc = new DailyCaloriesMongo();
        mongoDoc.setId(UUID.randomUUID().toString());
        mongoDoc.setUserId(user.getUserId());
        mongoDoc.setDate(LocalDate.now());
        mongoDoc.setConsumedCalories(dailyCalories.getConsumedCalories());
        mongoDoc.setConsumedProteins(dailyCalories.getConsumedProteins());
        mongoDoc.setConsumedFats(dailyCalories.getConsumedFats());
        mongoDoc.setConsumedCarbs(dailyCalories.getConsumedCarbs());
        mongoRepo.save(mongoDoc);
        userProducer.send(buildEvent(userId, dailyCalories));
        return buildFullResponse(dailyCalories);
    }

    private DailyCalories getOrCreateDailyRecord(Long userId, UserDTO user) {
        LocalDate today = LocalDate.now();
        return calorieRepo.findByUserIdAndDate(userId, today) .orElseGet(() -> createDailyRecord(user, userId, today));
    }

    private DailyCalories createDailyRecord(UserDTO user,Long userId, LocalDate date) {
        DailyCalories record = DailyCalories.builder()
                .userId(userId)
                .date(date)
                .dailyNorm(user.getCaloriesNorm())
                .consumedCalories(0.0)
                .proteinNorm(user.getProteinNorm())
                .fatNorm(user.getFatNorm())
                .carbsNorm(user.getCarbsNorm())
                .consumedProteins(0.0)
                .consumedFats(0.0)
                .consumedCarbs(0.0)
                .build();

        calorieRepo.save(record);
        return record;
    }

    private CalorieDifferenceResponse buildFullResponse(DailyCalories dailyCalories) {
        return CalorieDifferenceResponse.builder()
                .date(dailyCalories.getDate())
                .consumedCalories(round(dailyCalories.getConsumedCalories()))
                .consumedProteins(round(dailyCalories.getConsumedProteins()))
                .consumedFats(round(dailyCalories.getConsumedFats()))
                .consumedCarbs(round(dailyCalories.getConsumedCarbs()))
                .build();
    }

    private double round(double value) {
        return Math.round(value);
    }

    private ConsumedEvent buildEvent(Long userId, DailyCalories dailyCalories) {
        ConsumedEvent event = ConsumedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now())
                .userId(userId)
                .calories(dailyCalories.getConsumedCalories())
                .proteins(dailyCalories.getConsumedProteins())
                .fats(dailyCalories.getConsumedFats())
                .carbs(dailyCalories.getConsumedCarbs())
                .version(1)
                .status(Status.ACTIVE)
                .build();
        KafkaMessageLogEntity logEntity = new KafkaMessageLogEntity();
        logEntity.setMessageKey(userId.toString());
        logEntity.setPayload(convertToJson(event));
        logEntity.setStatus("PENDING");
        logEntity.setSentAt(LocalDateTime.now());
        logEntity.setUpdatedAt(LocalDateTime.now());
        kafkaMessageLogRepository.save(logEntity);
        return event;
    }

    private String convertToJson(ConsumedEvent dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (Exception e) {
            throw new RuntimeException("JSON serialization error", e);
        }
    }

    public DailyCalories getDailyCalories(Long userId, LocalDate date) {
        return calorieRepo.findByUserIdAndDate(userId,date).orElseThrow(
                () -> new UserNotFoundException("User not found: " + userId));
    }
}
