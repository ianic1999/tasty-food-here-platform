package com.example.tfhbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingDTO {
    private Long id;
    private LocalDateTime time;
    private Integer duration;
    private TableDTO table;
    private List<OrderDTO> orders;
}
