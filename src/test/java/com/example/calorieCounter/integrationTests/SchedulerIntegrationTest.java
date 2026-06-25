package com.example.calorieCounter.integrationTests;

import com.example.calorieCounter.entity.KafkaMessageLogEntity;
import com.example.calorieCounter.repository.CalorieRepo;
import com.example.calorieCounter.repository.KafkaMessageLogRepository;
import com.example.calorieCounter.scheduler.KafkaOutboxScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"food-topic"})
public class SchedulerIntegrationTest {

    @Autowired
    private KafkaMessageLogRepository logRepository;

    @Autowired
    private KafkaOutboxScheduler scheduler;

    @Test
    void shouldProcessOutboxAndSendToKafka() throws Exception {

        KafkaMessageLogEntity log = new KafkaMessageLogEntity();
        log.setPayload("""
        {
          "eventId":"1",
          "userId":1
        }
        """);
        log.setStatus("PENDING");
        logRepository.save(log);
        scheduler.processOutbox();
        KafkaMessageLogEntity updated =
                logRepository.findById(log.getId()).orElseThrow();
        assertEquals("SENT", updated.getStatus());
    }
}
