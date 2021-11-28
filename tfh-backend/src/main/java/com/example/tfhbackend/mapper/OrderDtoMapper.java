package com.example.tfhbackend.mapper;

import com.example.tfhbackend.dto.BookingDTO;
import com.example.tfhbackend.dto.MenuItemDTO;
import com.example.tfhbackend.dto.OrderDTO;
import com.example.tfhbackend.model.Booking;
import com.example.tfhbackend.model.MenuItem;
import com.example.tfhbackend.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderDtoMapper implements Mapper<Order, OrderDTO> {
    private final Mapper<MenuItem, MenuItemDTO> menuItemMapper;
    private final Mapper<Booking, BookingDTO> bookingMapper;

    @Override
    public OrderDTO map(Order entity) {
        return OrderDTO.builder()
                .id(entity.getId())
                .booking(bookingMapper.map(entity.getBooking()))
                .items(menuItemMapper.mapList(entity.getItems()))
                .build();
    }
}
