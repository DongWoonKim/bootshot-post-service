package com.example.spring.postservice.service;

import com.example.spring.postservice.dto.PostCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "post-events";

    public void sendPostCreatedEvent(PostCreatedEvent event) {
        try {
            log.info("Sending post created event to topic: {}, {}", TOPIC, event.toString());
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, event.aggregate_id(), message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Kafka 직렬화 오류", e);
        }
    }
}
