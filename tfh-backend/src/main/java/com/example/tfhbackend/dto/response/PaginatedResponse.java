package com.example.tfhbackend.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PaginatedResponse<T> {
    private List<T> data;
    private Pagination pagination;

    public PaginatedResponse(Page<T> page) {
        data = page.toList();
        pagination = Pagination.builder()
                .total(page.getTotalPages())
                .currentPage(page.getNumber() + 1)
                .count(page.getNumberOfElements())
                .perPage(page.getSize())
                .links(new Links(!page.isFirst(), !page.isLast()))
                .build();
    }
}
