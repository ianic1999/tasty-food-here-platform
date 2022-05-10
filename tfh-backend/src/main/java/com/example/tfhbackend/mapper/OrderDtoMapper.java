package com.example.tfhbackend.mapper;

import com.example.tfhbackend.dto.MenuItemDTO;
import com.example.tfhbackend.dto.MenuItemWithCountDTO;
import com.example.tfhbackend.dto.OrderDTO;
import com.example.tfhbackend.model.MenuItem;
import com.example.tfhbackend.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class OrderDtoMapper implements Mapper<Order, OrderDTO> {
    private final Mapper<MenuItem, MenuItemDTO> menuItemMapper;

    @Override
    public OrderDTO map(Order entity) {
        return OrderDTO.builder()
                       .id(entity.getId())
                       .bookingId(entity.getBooking().getId())
                       .items(getItemsWithCount(entity.getItems()))
                       .build();
    }

    private List<MenuItemWithCountDTO> getItemsWithCount(List<MenuItem> items) {
        return items.stream()
                    .collect(Collectors.groupingBy(MenuItem::getId))
                    .values()
                    .stream()
                    .map(this::getItemWithCount)
                    .collect(Collectors.toList());
    }

    private MenuItemWithCountDTO getItemWithCount(List<MenuItem> items) {
        return new MenuItemWithCountDTO(
                menuItemMapper.map(items.get(0)),
                items.size(),
                items.get(0).getPrice() * items.size()
        );
    }
}
