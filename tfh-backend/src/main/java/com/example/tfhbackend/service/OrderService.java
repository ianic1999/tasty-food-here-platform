package com.example.tfhbackend.service;

import com.example.tfhbackend.dto.OrderDTO;
import com.example.tfhbackend.dto.request.OrderRequest;
import org.springframework.data.domain.Page;

public interface OrderService {
    Page<OrderDTO> get(int page, int perPage);
    OrderDTO getById(Long id);
    OrderDTO add(OrderRequest request);
    void remove(Long id);
}
