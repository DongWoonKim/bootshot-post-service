package com.example.spring.postservice.dto;

public record PostCreatedEvent(
        String postId,
        String title,
        String content,
        String authorId
) {
    public String aggregate_id() {
        return postId;
    }
}