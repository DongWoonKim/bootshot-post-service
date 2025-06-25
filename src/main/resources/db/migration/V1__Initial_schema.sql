CREATE TABLE event_store (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     aggregate_id VARCHAR(100) NOT NULL,   -- 게시글 ID
     event_type VARCHAR(100) NOT NULL,     -- 이벤트 타입 (예: PostCreatedEvent)
     payload TEXT NOT NULL,                -- 이벤트 내용 (JSON)
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);