package com.example.tfhbackend.mapper;

import com.example.tfhbackend.dto.TableDTO;
import com.example.tfhbackend.model.Booking;
import com.example.tfhbackend.model.Table;
import com.example.tfhbackend.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class TableDtoMapper implements Mapper<Table, TableDTO> {

    private final BookingRepository bookingRepository;

    @Override
    public TableDTO map(Table entity) {
        TableDTO.TableDTOBuilder builder = TableDTO.builder()
                                                   .id(entity.getId())
                                                   .ordinalNumber(entity.getOrdinalNumber())
                                                   .nrOfSpots(entity.getNrOfSpots());

        bookingRepository.getActiveForTable(entity.getId())
                .map(Booking::getId)
                .ifPresent(builder::currentBookingId);

        return builder.build();
    }
}
