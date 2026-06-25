package com.example.calorieCounter.unitTests;

import com.example.calorieCounter.dto.CalorieIntakeBatchRequest;
import com.example.calorieCounter.dto.CalorieIntakeRequest;
import com.example.calorieCounter.dto.UserDTO;
import com.example.calorieCounter.dto.CalorieDifferenceResponse;
import com.example.calorieCounter.entity.Food;
import com.example.calorieCounter.exeption.FoodNotFoundException;
import com.example.calorieCounter.kafka.UserProducer;
import com.example.calorieCounter.repository.CalorieMongoRepo;
import com.example.calorieCounter.repository.CalorieRepo;
import com.example.calorieCounter.repository.FoodRepo;
import com.example.calorieCounter.repository.KafkaMessageLogRepository;
import com.example.calorieCounter.service.CalorieTrackingService;
import com.example.calorieCounter.service.UserCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalorieTrackingServiceTest {
    @Mock
    private CalorieRepo calorieRepo;
    @Mock
    private UserCache userCache;
    @Mock
    private FoodRepo foodRepo;
    @Mock
    private UserProducer userProducer;
    @Mock
    private KafkaMessageLogRepository kafkaMessageLogRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private CalorieMongoRepo mongoRepo;

    @InjectMocks
    private CalorieTrackingService service;

    @Test
    void shouldCalculateCaloriesAndSave() throws Exception {
        Long userId = 1L;

        UserDTO user = UserDTO.builder()
                .userId(userId)
                .caloriesNorm(2000.0)
                .proteinNorm(100.0)
                .fatNorm(70.0)
                .carbsNorm(250.0)
                .build();

        when(userCache.get(userId)).thenReturn(user);

        Food food = new Food();
        food.setProtein(10.0);
        food.setFat(5.0);
        food.setCarb(20.0);

        when(foodRepo.findByNameFood("rice"))
                .thenReturn(Optional.of(food));

        when(calorieRepo.findByUserIdAndDate(any(), any()))
                .thenReturn(Optional.empty());


        CalorieIntakeRequest item = new CalorieIntakeRequest();
        item.setFoodName("rice");
        item.setGrams(100.0);

        CalorieIntakeBatchRequest request = new CalorieIntakeBatchRequest();
        request.setItems(List.of(item));

        when(objectMapper.writeValueAsString(any()))
                .thenReturn("{}");

        CalorieDifferenceResponse response =
                service.addCaloriesAndGetDifference(userId, request);
        assertNotNull(response);
        assertEquals(165.0, response.getConsumedCalories());
        verify(calorieRepo, times(2)).save(any());
        verify(mongoRepo).save(any());
        verify(userProducer).send(any());
        verify(kafkaMessageLogRepository).save(any());
    }

    @Test
    void shouldThrowIfUserNotFound() {
        when(userCache.get(1L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            service.addCaloriesAndGetDifference(1L, new CalorieIntakeBatchRequest());
        });
    }

    @Test
    void shouldThrowIfFoodNotFound() {
        when(userCache.get(any())).thenReturn(new UserDTO());
        when(foodRepo.findByNameFood(any()))
                .thenReturn(Optional.empty());

        CalorieIntakeRequest item = new CalorieIntakeRequest();
        item.setFoodName("unknown");

        CalorieIntakeBatchRequest request = new CalorieIntakeBatchRequest();
        request.setItems(List.of(item));

        assertThrows(FoodNotFoundException.class, () -> {
            service.addCaloriesAndGetDifference(1L, request);
        });
    }
}
