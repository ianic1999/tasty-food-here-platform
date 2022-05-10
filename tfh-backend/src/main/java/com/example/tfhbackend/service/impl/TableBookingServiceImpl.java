package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.TableDTO;
import com.example.tfhbackend.dto.TimeRangeDTO;
import com.example.tfhbackend.dto.request.TableBookingRequest;
import com.example.tfhbackend.mapper.Mapper;
import com.example.tfhbackend.model.Table;
import com.example.tfhbackend.repository.TableRepository;
import com.example.tfhbackend.service.TableBookingService;
import com.example.tfhbackend.validator.BookingTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
class TableBookingServiceImpl implements TableBookingService {

    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private static final String TIME_FORMAT = "HH:mm";

    private final TableRepository tableRepository;
    private final Mapper<Table, TableDTO> tableMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TableDTO> getFreeTables(TableBookingRequest request) {
        LocalDate date = LocalDate.parse(request.getDate(), DateTimeFormatter.ofPattern(DATE_FORMAT));
        LocalTime time = LocalTime.parse(request.getTime(), DateTimeFormatter.ofPattern(TIME_FORMAT));
        return tableMapper.mapList(tableRepository.getFreeTablesForTimeRange(date.atTime(time),
                                                                             date.atTime(time)
                                                                                    .plusMinutes(request.getDuration())));
    }

    @Override
    @Transactional(readOnly = true)
    public TimeRangeDTO getFirstAvailableTable(TableBookingRequest request) {
        LocalDate date = LocalDate.parse(request.getDate(), DateTimeFormatter.ofPattern(DATE_FORMAT));
        LocalTime time = LocalTime.parse(request.getTime(), DateTimeFormatter.ofPattern(TIME_FORMAT));
        LocalDateTime start = date.atTime(time);

        return IntStream.range(-request.getDuration(), request.getDuration())
                .filter(d -> d % 5 == 0)
                .mapToObj(start::plusMinutes)
                .sorted(Comparator.comparingInt(t -> (int) Math.abs(ChronoUnit.MINUTES.between(start, t))))
                .map(t -> Map.entry(t, tableRepository.isTimeAvailable(t, t.plusMinutes(request.getDuration()))))
                .filter(Map.Entry::getValue)
                .findFirst()
                .map(Map.Entry::getKey)
                .map(t -> new TimeRangeDTO(t.toLocalDate(), t.toLocalTime(), t.toLocalTime().plusMinutes(request.getDuration())))
                .orElse(null);

    }
}
