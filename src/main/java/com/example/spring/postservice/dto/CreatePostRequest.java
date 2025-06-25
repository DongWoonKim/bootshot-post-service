package com.example.spring.postservice.dto;

public record CreatePostRequest(
        String title,
        String content,
        String authorId
) {
}
