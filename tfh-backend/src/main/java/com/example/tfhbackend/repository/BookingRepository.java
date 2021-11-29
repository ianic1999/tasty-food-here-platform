package com.example.tfhbackend.repository;

import com.example.tfhbackend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
