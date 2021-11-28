package com.example.tfhbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderDTO {
    private Long id;
    private List<MenuItemDTO> items;
    private BookingDTO booking;

}
