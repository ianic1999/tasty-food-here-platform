package com.example.tfhbackend.mapper;

import com.example.tfhbackend.dto.MenuItemDTO;
import com.example.tfhbackend.model.MenuItem;
import org.springframework.stereotype.Component;

@Component
class MenuItemDtoMapper implements Mapper<MenuItem, MenuItemDTO> {

    private static final String URL = "https://tfh-iamges.fra1.digitaloceanspaces.com";

    @Override
    public MenuItemDTO map(MenuItem entity) {
        return MenuItemDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .image(URL  + "/" + entity.getImage())
                .category(entity.getCategory().getName())
                .build();
    }
}
