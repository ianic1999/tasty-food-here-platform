package com.example.tfhbackend.mapper;

import com.example.tfhbackend.dto.TableDTO;
import com.example.tfhbackend.model.Table;
import org.springframework.stereotype.Component;

@Component
class TableDtoMapper implements Mapper<Table, TableDTO> {

    @Override
    public TableDTO map(Table entity) {
        return TableDTO.builder()
                .id(entity.getId())
                .ordinalNumber(entity.getOrdinalNumber())
                .nrOfSpots(entity.getNrOfSpots())
                .build();
    }
}
