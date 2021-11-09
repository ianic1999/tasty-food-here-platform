package com.example.tfhbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Pagination {
    private int count;
    private int total;
    private int perPage;
    private int currentPage;
    private Links links;
}
