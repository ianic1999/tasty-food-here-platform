package com.example.tfhbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StatisticsDTO {
    private long nrOfTables;
    private long nrOfWaiters;
    private long nrOfBookings;
    private long nrOfFeedback;
}
