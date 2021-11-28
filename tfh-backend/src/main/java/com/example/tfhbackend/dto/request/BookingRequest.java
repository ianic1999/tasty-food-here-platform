package com.example.tfhbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingRequest {
    private Long id;
    private LocalDateTime time;
    private Integer duration;
    private Long tableId;
}
