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
    private String phone;
    private String fullName;
    private String date;
    private String time;
    private Integer duration;
    private Long tableId;
}
