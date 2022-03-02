package com.example.tfhbackend.service;

import com.example.tfhbackend.dto.OrderDTO;
import com.example.tfhbackend.dto.request.OrderRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    Page<OrderDTO> get(int page, int perPage);
    List<OrderDTO> getForTable(Long tableId);
    OrderDTO getById(Long id);
    OrderDTO add(OrderRequest request);
    void remove(Long id);
}
