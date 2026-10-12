package com.example.devops_demo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}