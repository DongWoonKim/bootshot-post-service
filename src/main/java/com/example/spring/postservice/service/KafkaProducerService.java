package com.example.spring.postservice.service;

import com.example.spring.postservice.domain.EventEntity;
import com.example.spring.postservice.dto.PostCreatedEvent;
import com.example.spring.postservice.repository.EventStoreRepository;
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
    private final EventStoreRepository eventStoreRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "post-events";

    public void sendPostCreatedEvent(PostCreatedEvent event) {
        String payload = serializeEvent(event);
        sendToKafka(event.postId(), payload);
        saveToEventStore(event, payload);
    }

    private String serializeEvent(PostCreatedEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("❌ 이벤트 직렬화 실패: " + event, e);
        }
    }

    private void sendToKafka(String key, String message) {
        kafkaTemplate.send(TOPIC, key, message);
        log.info("📤 Kafka 전송 완료 → topic: {}, key: {}, payload: {}", TOPIC, key, message);
    }

    private void saveToEventStore(Object event, String payload) {
        EventEntity entity = EventEntity.builder()
                .aggregateId(getAggregateId(event))
                .eventType(event.getClass().getSimpleName())
                .payload(payload)
                .build();

        eventStoreRepository.save(entity);
        log.info("💾 이벤트 저장 완료 → type: {}, aggregateId: {}", entity.getEventType(), entity.getAggregateId());
    }

    private String getAggregateId(Object event) {
        if (event instanceof PostCreatedEvent e) {
            return e.postId();
        }
        throw new IllegalArgumentException("지원하지 않는 이벤트 타입: " + event.getClass().getName());
    }

}
