package com.example.spring.postservice.controller;

import com.example.spring.postservice.dto.CreatePostRequest;
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
    public ResponseEntity<Void> createPost(@RequestBody CreatePostRequest request) {
        log.info("Received request to create post {}", request);
        String generatedPostId = UUID.randomUUID().toString();

        PostCreatedEvent event = new PostCreatedEvent(
                generatedPostId,
                request.title(),
                request.content(),
                request.authorId()
        );

        producerService.sendPostCreatedEvent(event);
        return ResponseEntity.ok().build();
    }

}
