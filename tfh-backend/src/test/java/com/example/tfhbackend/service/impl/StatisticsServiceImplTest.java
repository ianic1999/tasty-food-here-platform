package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.BookingsPerDayStatisticsDTO;
import com.example.tfhbackend.dto.BookingsPerMonthStatisticsDTO;
import com.example.tfhbackend.model.Booking;
import com.example.tfhbackend.model.MenuItem;
import com.example.tfhbackend.model.Table;
import com.example.tfhbackend.model.enums.UserRole;
import com.example.tfhbackend.model.fixture.BookingFixture;
import com.example.tfhbackend.model.fixture.MenuItemFixture;
import com.example.tfhbackend.model.fixture.TableFixture;
import com.example.tfhbackend.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsServiceImplTest {

    private final LocalDate today = LocalDate.of(2022, 1, 10);
    @Mock
    private TableRepository tableRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private FeedbackRepository feedbackRepository;
    @Mock
    private MenuItemRepository menuItemRepository;
    private StatisticsServiceImpl statisticsService;
    private MenuItem cola;
    private MenuItem pizza;
    private Table table;

    @Before
    public void setup() {
        statisticsService = spy(
                new StatisticsServiceImpl(
                        tableRepository,
                        userRepository,
                        bookingRepository,
                        feedbackRepository,
                        menuItemRepository
                )
        );
        when(statisticsService.getTodayDate())
                .thenReturn(today);

        cola = MenuItemFixture.cocaCola();
        pizza = MenuItemFixture.pizza();
        table = TableFixture.aTable();

        when(tableRepository.count()).thenReturn(10L);
        when(userRepository.countAllByRole(UserRole.WAITER)).thenReturn(10L);
        when(bookingRepository.count()).thenReturn(10L);
        when(feedbackRepository.count()).thenReturn(10L);
        when(menuItemRepository.findAll())
                .thenReturn(List.of(cola, pizza));
    }

    @Test
    public void getStatistics_whenInvoked_correctResult() {
        var result = statisticsService.getStatistics();

        assertThat(result.getNrOfTables()).isEqualTo(10);
        assertThat(result.getNrOfWaiters()).isEqualTo(10);
        assertThat(result.getNrOfBookings()).isEqualTo(10);
        assertThat(result.getNrOfFeedback()).isEqualTo(10);
    }

    @Test
    public void getItemsPerCategoriesStatistics_whenInvoked_correctResult() {
        var result = statisticsService.getItemsPerCategoriesStatistics();

        assertThat(result)
                .hasSize(2)
                .allSatisfy(item -> {
                    assertThat(item.getItems()).isEqualTo(1);
                    assertThat(item.getCategory()).isIn(pizza.getCategory().getName(), cola.getCategory().getName());
                });
    }

    @Test
    public void getBookingsPerDayStatistics_whenInvoked_correctResult() {
        final LocalDate date = LocalDate.of(2022, 1, 10);
        Booking first = BookingFixture.forTableAndDateTime(table, date, LocalTime.of(10, 0));
        Booking second = BookingFixture.forTableAndDateTime(table, date, LocalTime.of(14, 0));
        when(bookingRepository.findAll())
                .thenReturn(List.of(first, second));

        var result = statisticsService.getBookingsPerDayStatistics();

        assertThat(result)
                .hasSize(14)
                .isSortedAccordingTo(Comparator.comparingInt(BookingsPerDayStatisticsDTO::getHour));
        assertThat(result.stream().filter(elem -> elem.getBookings() != 0))
                .hasSize(2)
                .allSatisfy(item -> {
                    assertThat(item.getHour()).isIn(10, 14);
                    assertThat(item.getBookings()).isEqualTo(1);
                });
    }

    @Test
    public void getBookingsPerWeekStatistics_whenInvoked_correctResult() {
        Booking first = BookingFixture.forTableAndDateTime(table, LocalDate.of(2022, 1, 10), LocalTime.of(10, 0));
        Booking second = BookingFixture.forTableAndDateTime(table, LocalDate.of(2022, 1, 11), LocalTime.of(14, 0));
        when(bookingRepository.findAll())
                .thenReturn(List.of(first, second));

        var result = statisticsService.getBookingsPerWeekStatistics();

        assertThat(result)
                .hasSize(7)
                .isSortedAccordingTo(Comparator.comparingInt(a -> DayOfWeek.valueOf(a.getDay()).ordinal()));
        assertThat(result.stream().filter(elem -> elem.getBookings() != 0))
                .hasSize(2)
                .allSatisfy(item -> {
                    assertThat(item.getDay()).isIn(DayOfWeek.MONDAY.name(), DayOfWeek.TUESDAY.name());
                    assertThat(item.getBookings()).isEqualTo(1);
                });
    }

    @Test
    public void getBookingsPerMonthStatistics_whenInvoked_correctResult() {
        Booking first = BookingFixture.forTableAndDateTime(table, LocalDate.of(2022, 1, 10), LocalTime.of(10, 0));
        Booking second = BookingFixture.forTableAndDateTime(table, LocalDate.of(2022, 1, 11), LocalTime.of(14, 0));
        when(bookingRepository.findAll())
                .thenReturn(List.of(first, second));

        var result = statisticsService.getBookingsPerMonthStatistics();

        assertThat(result)
                .hasSize(31)
                .isSortedAccordingTo(Comparator.comparingInt(BookingsPerMonthStatisticsDTO::getDay));
        assertThat(result.stream().filter(elem -> elem.getBookings() != 0))
                .hasSize(2)
                .allSatisfy(item -> {
                    assertThat(item.getDay()).isIn(10, 11);
                    assertThat(item.getBookings()).isEqualTo(1);
                });
    }

    @Test
    public void getBookingsPerMonthStatistics_whenFebruaryAndLeapYear_correctResult() {
        Booking first = BookingFixture.forTableAndDateTime(table, LocalDate.of(2024, 2, 10), LocalTime.of(10, 0));
        Booking second = BookingFixture.forTableAndDateTime(table, LocalDate.of(2024, 2, 11), LocalTime.of(14, 0));
        when(bookingRepository.findAll())
                .thenReturn(List.of(first, second));
        when(statisticsService.getTodayDate())
                .thenReturn(LocalDate.of(2024, 2, 20));

        var result = statisticsService.getBookingsPerMonthStatistics();

        assertThat(result)
                .hasSize(29)
                .isSortedAccordingTo(Comparator.comparingInt(BookingsPerMonthStatisticsDTO::getDay));
        assertThat(result.stream().filter(elem -> elem.getBookings() != 0))
                .hasSize(2)
                .allSatisfy(item -> {
                    assertThat(item.getDay()).isIn(10, 11);
                    assertThat(item.getBookings()).isEqualTo(1);
                });
    }

}