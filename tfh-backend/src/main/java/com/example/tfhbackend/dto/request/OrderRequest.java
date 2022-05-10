package com.example.tfhbackend.dto.request;

import com.example.tfhbackend.dto.MenuItemWithQuantityDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderRequest {
    private Long id;
    private Long bookingId;
    private List<MenuItemWithQuantityDTO> items;
}
