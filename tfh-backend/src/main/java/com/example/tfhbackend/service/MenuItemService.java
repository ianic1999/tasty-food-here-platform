package com.example.tfhbackend.service;

import com.example.tfhbackend.dto.MenuItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MenuItemService {
    Page<MenuItemDTO> get(int page, int perPage);
    MenuItemDTO getById(Long id);
    MenuItemDTO add(String name,
                    Double price,
                    String category,
                    MultipartFile image) throws IOException;
    MenuItemDTO update(Long id,
                       String name,
                       Double price,
                       String category,
                       MultipartFile image) throws IOException;
    void remove(Long id) throws IOException;
}
