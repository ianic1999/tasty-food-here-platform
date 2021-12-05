package com.example.tfhbackend.mapper;

import com.example.tfhbackend.dto.BookingDTO;
import com.example.tfhbackend.dto.OrderDTO;
import com.example.tfhbackend.dto.TableDTO;
import com.example.tfhbackend.model.Booking;
import com.example.tfhbackend.model.Order;
import com.example.tfhbackend.model.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class BookingDtoMapper implements Mapper<Booking, BookingDTO> {
    private final Mapper<Table, TableDTO> tableMapper;
    private final Mapper<Order, OrderDTO> orderMapper;

    @Override
    public BookingDTO map(Booking entity) {
        return BookingDTO.builder()
                .id(entity.getId())
                .duration(entity.getDuration())
                .time(entity.getTime())
                .table(tableMapper.map(entity.getTable()))
                .orders(orderMapper.mapList(entity.getOrders()))
                .build();
    }
}
