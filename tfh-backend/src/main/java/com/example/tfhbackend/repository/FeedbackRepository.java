package com.example.tfhbackend.repository;

import com.example.tfhbackend.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Optional<Feedback> findByBookingId(String bookingId);
}
