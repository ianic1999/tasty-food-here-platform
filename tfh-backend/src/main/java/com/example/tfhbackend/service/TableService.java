package com.example.tfhbackend.service;

import com.example.tfhbackend.dto.TableDTO;
import org.springframework.data.domain.Page;

public interface TableService {
    Page<TableDTO> get(int page, int perPage);
    TableDTO getById(Long id);
    TableDTO add(TableDTO table);
    TableDTO update(TableDTO table);
    void remove(Long id);
}
