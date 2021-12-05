package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.MenuItemDTO;
import com.example.tfhbackend.mapper.Mapper;
import com.example.tfhbackend.model.MenuItem;
import com.example.tfhbackend.model.enums.FoodCategory;
import com.example.tfhbackend.model.exception.EntityNotFoundException;
import com.example.tfhbackend.repository.MenuItemRepository;
import com.example.tfhbackend.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final Mapper<MenuItem, MenuItemDTO> mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemDTO> get(int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        return menuItemRepository.findAll(pageable).map(mapper::map);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemDTO getById(Long id) {
        return mapper.map(findMenuItemById(id));
    }

    @Override
    @Transactional
    public MenuItemDTO add(String name,
                           Double price,
                           String category,
                           MultipartFile image) {
        MenuItem menuItem = MenuItem.builder()
                .name(name)
                .price(price)
                .category(FoodCategory.valueOf(category))
                .orders(new ArrayList<>())
                .build();
        return mapper.map(menuItemRepository.save(menuItem));
    }

    @Override
    @Transactional
    public MenuItemDTO update(Long id,
                              String name,
                              Double price,
                              String category,
                              MultipartFile image) {
        MenuItem menuItem = findMenuItemById(id);
        Optional.ofNullable(name).ifPresent(menuItem::setName);
        Optional.ofNullable(price).ifPresent(menuItem::setPrice);
        Optional.ofNullable(category).map(FoodCategory::valueOf).ifPresent(menuItem::setCategory);
        return mapper.map(menuItem);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        menuItemRepository.deleteById(id);
    }

    private MenuItem findMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu item with id " + id + " not found"));
    }
}
