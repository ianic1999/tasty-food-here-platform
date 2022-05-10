package com.example.tfhbackend.validator;

import com.example.tfhbackend.model.exception.CustomRuntimeException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class BookingTimeValidator {

    private static final LocalTime CLOSE_TIME = LocalTime.of(23, 0);

    public void validate(LocalDateTime start, LocalDateTime end) {
        validateEndAfterStart(start, end);
        validateStartInFuture(start);
        validateEndIsTheSameDate(start, end);
        validateEndIsBeforeClosing(end);
    }

    private void validateEndAfterStart(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start) || end.isEqual(start)) {
            throw new CustomRuntimeException("End time should be after start time");
        }
    }

    private void validateStartInFuture(LocalDateTime start) {
        if (LocalDateTime.now().plusMinutes(15).isAfter(start))
            throw new CustomRuntimeException("Booking can be done with at least 15 minutes in advance");
    }

    private void validateEndIsTheSameDate(LocalDateTime start, LocalDateTime end) {
        if (!start.toLocalDate().isEqual(end.toLocalDate()))
            throw new CustomRuntimeException("Booking end time is after closing");
    }

    private void validateEndIsBeforeClosing(LocalDateTime end) {
        if (end.toLocalTime().isAfter(CLOSE_TIME))
            throw new CustomRuntimeException("Booking end time is after closing");
    }
}
