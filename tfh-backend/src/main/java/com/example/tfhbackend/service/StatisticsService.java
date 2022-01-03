package com.example.tfhbackend.service;

import com.example.tfhbackend.dto.*;

import java.util.List;

public interface StatisticsService {
    StatisticsDTO getStatistics();
    List<ItemsPerCategoriesStatisticsDTO> getItemsPerCategoriesStatistics();
    List<BookingsPerDayStatisticsDTO> getBookingsPerDayStatistics();
    List<BookingsPerWeekStatisticsDTO> getBookingsPerWeekStatistics();
    List<BookingsPerMonthStatisticsDTO> getBookingsPerMonthStatistics();
}
