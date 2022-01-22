package com.example.tfhbackend.controller;

import com.example.tfhbackend.dto.*;
import com.example.tfhbackend.service.StatisticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private StatisticsService statisticsService;

    private MockMvc mockMvc;

    private StatisticsDTO statistics;
    private ItemsPerCategoriesStatisticsDTO itemsPerCategoriesStatisticsDTO;
    private BookingsPerDayStatisticsDTO bookingsPerDayStatisticsDTO;
    private BookingsPerWeekStatisticsDTO bookingsPerWeekStatisticsDTO;
    private BookingsPerMonthStatisticsDTO bookingsPerMonthStatisticsDTO;

    @Before
    public void setup() {
        StatisticsController controller = new StatisticsController(statisticsService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        statistics = new StatisticsDTO(
                1,
                2,
                3,
                4
        );
        itemsPerCategoriesStatisticsDTO = new ItemsPerCategoriesStatisticsDTO(
                "BEVERAGE", 10
        );
        bookingsPerDayStatisticsDTO = new BookingsPerDayStatisticsDTO(10, 3);
        bookingsPerWeekStatisticsDTO = new BookingsPerWeekStatisticsDTO("MONDAY", 10);
        bookingsPerMonthStatisticsDTO = new BookingsPerMonthStatisticsDTO(10, 15);

        when(statisticsService.getStatistics()).thenReturn(statistics);
        when(statisticsService.getItemsPerCategoriesStatistics()).thenReturn(List.of(itemsPerCategoriesStatisticsDTO));
        when(statisticsService.getBookingsPerDayStatistics()).thenReturn(List.of(bookingsPerDayStatisticsDTO));
        when(statisticsService.getBookingsPerWeekStatistics()).thenReturn(List.of(bookingsPerWeekStatisticsDTO));
        when(statisticsService.getBookingsPerMonthStatistics()).thenReturn(List.of(bookingsPerMonthStatisticsDTO));
    }

    @Test
    public void get_whenInvoked_correctResponse() throws Exception {
        mockMvc.perform(get("/api/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nrOfTables", is(1)))
                .andExpect(jsonPath("$.data.nrOfWaiters", is(2)))
                .andExpect(jsonPath("$.data.nrOfBookings", is(3)))
                .andExpect(jsonPath("$.data.nrOfFeedback", is(4)));
    }

    @Test
    public void getItemsPerCategoriesStatistics_whenInvoked_correctResponse() throws Exception {
        mockMvc.perform(get("/api/statistics/items_per_categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].category", is(itemsPerCategoriesStatisticsDTO.getCategory())))
                .andExpect(jsonPath("$.data[0].items", is(10)));
    }

    @Test
    public void getBookingsPerDay_whenInvoked_correctResponse() throws Exception {
        mockMvc.perform(get("/api/statistics/bookings_per_day"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].hour", is(10)))
                .andExpect(jsonPath("$.data[0].bookings", is(3)));
    }

    @Test
    public void getBookingsPerWeek_whenInvoked_correctResponse() throws Exception {
        mockMvc.perform(get("/api/statistics/bookings_per_week"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].day", is(bookingsPerWeekStatisticsDTO.getDay())))
                .andExpect(jsonPath("$.data[0].bookings", is(10)));
    }

    @Test
    public void getBookingsPerMonth_whenInvoked_correctResponse() throws Exception {
        mockMvc.perform(get("/api/statistics/bookings_per_month"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].day", is(10)))
                .andExpect(jsonPath("$.data[0].bookings", is(15)));
    }

}