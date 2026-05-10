package com.example.calorieCounter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@Data
@Entity
@Table(name = "kafka_message_log")
@AllArgsConstructor
public class KafkaMessageLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String payload;
    @Column(name = "message_key")
    private String messageKey;
    @Column
    private String status;
    @Column(name = "sent_at")
    private LocalDateTime sentAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
