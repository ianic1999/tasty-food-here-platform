package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.TableDTO;
import com.example.tfhbackend.dto.request.TableBookingRequest;
import com.example.tfhbackend.mapper.Mapper;
import com.example.tfhbackend.model.Table;
import com.example.tfhbackend.repository.TableRepository;
import com.example.tfhbackend.service.TableBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
}
