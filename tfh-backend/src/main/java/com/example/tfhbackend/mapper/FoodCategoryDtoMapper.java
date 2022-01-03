package com.example.tfhbackend.mapper;

import com.example.tfhbackend.dto.FoodCategoryDTO;
import com.example.tfhbackend.model.enums.FoodCategory;
import org.springframework.stereotype.Component;

@Component
class FoodCategoryDtoMapper implements Mapper<FoodCategory, FoodCategoryDTO> {

    @Override
    public FoodCategoryDTO map(FoodCategory entity) {
        return FoodCategoryDTO.builder()
                .key(entity.name())
                .name(entity.getName())
                .build();
    }
}
