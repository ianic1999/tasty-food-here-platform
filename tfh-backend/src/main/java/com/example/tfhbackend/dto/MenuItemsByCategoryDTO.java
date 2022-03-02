package com.example.tfhbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MenuItemsByCategoryDTO {
    private String category;
    private List<MenuItemDTO> items;
}
