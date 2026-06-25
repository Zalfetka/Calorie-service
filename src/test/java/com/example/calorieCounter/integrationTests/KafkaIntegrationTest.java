package com.example.calorieCounter.integrationTests;

import com.example.calorieCounter.entity.DailyCalories;
import com.example.calorieCounter.repository.CalorieRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"food-topic"})
public class KafkaIntegrationTest {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private CalorieRepo calorieRepo;

    @Test
    void shouldConsumeMessageAndSaveToDb() throws Exception {
        String message = """
        {
          "eventId":"1",
          "timestamp":"2026-01-01T10:00:00",
          "userId":1,
          "calories":100,
          "proteins":10,
          "fats":5,
          "carbs":20,
          "version":1,
          "status":"ACTIVE"
        }
        """;

        kafkaTemplate.send("food-topic", message);
        Thread.sleep(2000);
        List<DailyCalories> all = calorieRepo.findAll();
        assertFalse(all.isEmpty());
    }
}
