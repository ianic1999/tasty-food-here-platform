package com.example.tfhbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TableDTO {
    private Long id;
    private Integer ordinalNumber;
    private Integer nrOfSpots;
}
