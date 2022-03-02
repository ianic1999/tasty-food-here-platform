package com.example.tfhbackend.service;

import com.example.tfhbackend.dto.TableDTO;
import com.example.tfhbackend.dto.UserDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TableService {
    Page<TableDTO> get(int page, int perPage);
    List<TableDTO> getForWaiter(UserDTO currentUser);
    TableDTO getById(Long id);
    TableDTO add(TableDTO table);
    TableDTO update(TableDTO table);
    void remove(Long id);
}
