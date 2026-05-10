package com.example.calorieCounter.scheduler;

import com.example.calorieCounter.dto.ConsumedEvent;
import com.example.calorieCounter.dto.UserDTO;
import com.example.calorieCounter.entity.KafkaMessageLogEntity;
import com.example.calorieCounter.repository.KafkaMessageLogRepository;
import com.example.calorieCounter.kafka.UserProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class KafkaOutboxScheduler {

    private final KafkaMessageLogRepository repository;
    private final UserProducer userProducer;
    private final ObjectMapper objectMapper;

    @Transactional
    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        List<KafkaMessageLogEntity> messages = repository.findByStatus("PENDING");
        for (KafkaMessageLogEntity message : messages) {
            try {
                ConsumedEvent dto = convertFromJson(message.getPayload());
                userProducer.send(dto);
                message.setStatus("SENT");
            } catch (Exception e) {
                message.setStatus("FAILED");
            }
            message.setUpdatedAt(LocalDateTime.now());
        }
        repository.saveAll(messages);
    }

    private ConsumedEvent convertFromJson(String json) {
        try {
            return objectMapper.readValue(json, ConsumedEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("JSON deserialization error", e);
        }
    }
}
