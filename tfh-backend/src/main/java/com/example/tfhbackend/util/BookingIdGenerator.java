package com.example.tfhbackend.util;

import liquibase.util.StringUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookingIdGenerator {

    public String generate(Long bookingId) {
        StringBuilder referenceId = new StringBuilder(bookingId.toString());
        while (referenceId.length() < 6) {
            referenceId.insert(0, "0");
        }
        return referenceId.toString();
    }
}
