package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.*;
import com.example.tfhbackend.model.Booking;
import com.example.tfhbackend.model.MenuItem;
import com.example.tfhbackend.model.enums.UserRole;
import com.example.tfhbackend.repository.*;
import com.example.tfhbackend.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
class StatisticsServiceImpl implements StatisticsService {
    private final TableRepository tableRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final FeedbackRepository feedbackRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    @Transactional(readOnly = true)
    public StatisticsDTO getStatistics() {
        return StatisticsDTO.builder()
                .nrOfTables(tableRepository.count())
                .nrOfWaiters(userRepository.countAllByRole(UserRole.WAITER))
                .nrOfBookings(bookingRepository.count())
                .nrOfFeedback(feedbackRepository.count())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemsPerCategoriesStatisticsDTO> getItemsPerCategoriesStatistics() {
        return menuItemRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(MenuItem::getCategory))
                .values()
                .stream()
                .map(this::mapMenuItemsToStatisticsDto)
                .collect(Collectors.toList());
    }

    private ItemsPerCategoriesStatisticsDTO mapMenuItemsToStatisticsDto(List<MenuItem> items) {
        return ItemsPerCategoriesStatisticsDTO.builder()
                .category(items.get(0).getCategory().getName())
                .items(items.size())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingsPerDayStatisticsDTO> getBookingsPerDayStatistics() {
        LocalDate today = getTodayDate();
        var statistics = bookingRepository.findAll()
                .stream()
                .filter(booking -> booking.getTime().toLocalDate().isEqual(today))
                .collect(Collectors.groupingBy(booking -> booking.getTime().getHour()))
                .values()
                .stream()
                .map(this::mapBookingsToBookingsPerDayStatistics)
                .collect(Collectors.toList());
        addMissingHoursToStatistics(statistics);
        return statistics;
    }

    private BookingsPerDayStatisticsDTO mapBookingsToBookingsPerDayStatistics(List<Booking> bookings) {
        return BookingsPerDayStatisticsDTO.builder()
                .hour(bookings.get(0).getTime().getHour())
                .bookings(bookings.size())
                .build();
    }

    private void addMissingHoursToStatistics(List<BookingsPerDayStatisticsDTO> statistics) {
        IntStream.rangeClosed(10, 23)
                .filter(hour -> statistics.stream().noneMatch(stat -> stat.getHour() == hour))
                .mapToObj(hour -> new BookingsPerDayStatisticsDTO(hour, 0))
                .forEach(statistics::add);
        statistics.sort(Comparator.comparingInt(BookingsPerDayStatisticsDTO::getHour));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingsPerWeekStatisticsDTO> getBookingsPerWeekStatistics() {
        var statistics = bookingRepository.findAll()
                .stream()
                .filter(booking -> this.isDateInThisWeek(booking.getTime().toLocalDate()))
                .collect(Collectors.groupingBy(booking -> booking.getTime().getDayOfWeek()))
                .values()
                .stream()
                .map(this::mapBookingsToBookingsPerWeekStatistics)
                .collect(Collectors.toList());
        addMissingWeekDaysToStatistics(statistics);
        return statistics;
    }

    private BookingsPerWeekStatisticsDTO mapBookingsToBookingsPerWeekStatistics(List<Booking> bookings) {
        return BookingsPerWeekStatisticsDTO.builder()
                .day(bookings.get(0).getTime().getDayOfWeek().name())
                .bookings(bookings.size())
                .build();
    }

    private void addMissingWeekDaysToStatistics(List<BookingsPerWeekStatisticsDTO> statistics) {
        Arrays.stream(DayOfWeek.values())
                .map(DayOfWeek::name)
                .filter(day -> statistics.stream().noneMatch(stat -> stat.getDay().equals(day)))
                .map(day -> new BookingsPerWeekStatisticsDTO(day, 0))
                .forEach(statistics::add);
        statistics.sort(Comparator.comparing(stat -> DayOfWeek.valueOf(stat.getDay()).ordinal()));
    }

    private boolean isDateInThisWeek(LocalDate date) {
        LocalDate today = getTodayDate();
        return today.minusDays(today.getDayOfWeek().getValue()).isBefore(date)
                && today.plusDays(8 - today.getDayOfWeek().getValue()).isAfter(date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingsPerMonthStatisticsDTO> getBookingsPerMonthStatistics() {
        LocalDate today = getTodayDate();
        var statistics = bookingRepository.findAll()
                .stream()
                .filter(booking -> booking.getTime().getMonthValue() == today.getMonthValue())
                .collect(Collectors.groupingBy(booking -> booking.getTime().getDayOfMonth()))
                .values()
                .stream()
                .map(this::mapBookingsToBookingsPerMonthStatistics)
                .collect(Collectors.toList());
        addMissingMonthDaysToStatistics(statistics);
        return statistics;
    }

    private BookingsPerMonthStatisticsDTO mapBookingsToBookingsPerMonthStatistics(List<Booking> bookings) {
        return BookingsPerMonthStatisticsDTO.builder()
                .day(bookings.get(0).getTime().getDayOfMonth())
                .bookings(bookings.size())
                .build();
    }

    private void addMissingMonthDaysToStatistics(List<BookingsPerMonthStatisticsDTO> statistics) {
        LocalDate today = getTodayDate();
        int nrOfDays = today.getMonth().length(today.getYear() % 4 == 0);
        IntStream.rangeClosed(1, nrOfDays)
                .filter(day -> statistics.stream().noneMatch(stat -> stat.getDay() == day))
                .mapToObj(day -> new BookingsPerMonthStatisticsDTO(day, 0))
                .forEach(statistics::add);
        statistics.sort(Comparator.comparingInt(BookingsPerMonthStatisticsDTO::getDay));
    }

    LocalDate getTodayDate() {
        return LocalDate.now();
    }
}
