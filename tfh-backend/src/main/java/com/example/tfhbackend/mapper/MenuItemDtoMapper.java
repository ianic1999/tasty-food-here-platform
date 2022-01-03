package com.example.tfhbackend.mapper;

import com.example.tfhbackend.dto.MenuItemDTO;
import com.example.tfhbackend.model.MenuItem;
import org.springframework.stereotype.Component;

@Component
class MenuItemDtoMapper implements Mapper<MenuItem, MenuItemDTO> {

    @Override
    public MenuItemDTO map(MenuItem entity) {
        return MenuItemDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .image(entity.getImage())
                .category(entity.getCategory().getName())
                .build();
    }
}
