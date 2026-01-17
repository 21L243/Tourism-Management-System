package com.ey.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}