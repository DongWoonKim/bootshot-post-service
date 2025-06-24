package com.example.spring.postservice.controller;

import com.example.spring.postservice.dto.PostCreatedEvent;
import com.example.spring.postservice.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class PostApiController {

    private final KafkaProducerService producerService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody Map<String, String> req) {
        log.info("Received request to create post {}", req);
        PostCreatedEvent event = new PostCreatedEvent(
                UUID.randomUUID().toString(),
                req.get("title"),
                req.get("content"),
                req.get("authorId")
        );

        producerService.sendPostCreatedEvent(event);
        return ResponseEntity.ok().build();
    }

}
