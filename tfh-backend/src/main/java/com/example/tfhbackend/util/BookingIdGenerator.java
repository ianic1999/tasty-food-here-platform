package com.example.tfhbackend.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookingIdGenerator {

    public String generate() {
        return LocalDateTime.now()
                            .toString()
                            .replaceAll("[^0-9]", "")
                .substring(0, 14);
    }
}
