package com.example.tfhbackend.controller;

import com.example.tfhbackend.dto.*;
import com.example.tfhbackend.dto.response.Response;
import com.example.tfhbackend.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin("*")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<Response<StatisticsDTO>> get() {
        return ResponseEntity.ok(
                new Response<>(statisticsService.getStatistics())
        );
    }

    @GetMapping("/items_per_categories")
    public ResponseEntity<Response<List<ItemsPerCategoriesStatisticsDTO>>> getItemsPerCategoriesStatistics() {
        return ResponseEntity.ok(
                new Response<>(statisticsService.getItemsPerCategoriesStatistics())
        );
    }

    @GetMapping("/bookings_per_day")
    public ResponseEntity<Response<List<BookingsPerDayStatisticsDTO>>> getBookingsPerDay() {
        return ResponseEntity.ok(
                new Response<>(statisticsService.getBookingsPerDayStatistics())
        );
    }

    @GetMapping("/bookings_per_week")
    public ResponseEntity<Response<List<BookingsPerWeekStatisticsDTO>>> getBookingsPerWeek() {
        return ResponseEntity.ok(
                new Response<>(statisticsService.getBookingsPerWeekStatistics())
        );
    }

    @GetMapping("/bookings_per_month")
    public ResponseEntity<Response<List<BookingsPerMonthStatisticsDTO>>> getBookingsPerMonth() {
        return ResponseEntity.ok(
                new Response<>(statisticsService.getBookingsPerMonthStatistics())
        );
    }
}
