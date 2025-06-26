package com.example.spring.postservice.repository;

import com.example.spring.postservice.domain.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventStoreRepository extends JpaRepository<EventEntity, Long> {
}
