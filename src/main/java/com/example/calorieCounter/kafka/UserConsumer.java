package com.example.calorieCounter.kafka;

import com.example.calorieCounter.dto.UserDTO;
import com.example.calorieCounter.enums.Status;
import com.example.calorieCounter.service.UserCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {
    private final ObjectMapper objectMapper;
    private final UserCache userCache;

    @KafkaListener(topics = "user-topic")
    public void listen(String message) throws JsonProcessingException {
        UserDTO event = objectMapper.readValue(message, UserDTO.class);

        if (event.getVersion() == 1 && event.getStatus() == Status.ACTIVE) {
            userCache.put(event);
        } else {
            log.warn("Unsupported or inactive event: {}", event);
        }
    }
}