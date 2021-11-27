package com.example.tfhbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MenuItemDTO {
    private Long id;
    private String name;
    private Double price;
    private String image;
    private String category;
}
