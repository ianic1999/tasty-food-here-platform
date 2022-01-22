package com.example.tfhbackend.model.fixture;

import com.example.tfhbackend.model.Booking;
import com.example.tfhbackend.model.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BookingFixture {

    public static Booking forTable(Table table) {
        return Booking.builder()
                .time(LocalDateTime.of(
                        LocalDate.of(2022, 1, 10),
                        LocalTime.of(10, 0, 0)
                ))
                .duration(120)
                .table(table)
                .build();
    }

    public static Booking forTableAndDateTime(Table table, LocalDate date, LocalTime time) {
        return Booking.builder()
                .time(LocalDateTime.of(date, time))
                .duration(120)
                .table(table)
                .build();
    }

}
