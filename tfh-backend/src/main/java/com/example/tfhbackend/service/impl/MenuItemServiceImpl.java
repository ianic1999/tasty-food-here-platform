package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.MenuItemDTO;
import com.example.tfhbackend.dto.MenuItemsByCategoryDTO;
import com.example.tfhbackend.mapper.Mapper;
import com.example.tfhbackend.model.MenuItem;
import com.example.tfhbackend.model.enums.FoodCategory;
import com.example.tfhbackend.model.exception.EntityNotFoundException;
import com.example.tfhbackend.repository.MenuItemRepository;
import com.example.tfhbackend.service.MenuItemService;
import com.example.tfhbackend.util.ImageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class MenuItemServiceImpl implements MenuItemService {

    private static final String IMAGE_FOLDER = "menu";

    private final MenuItemRepository menuItemRepository;
    private final ImageHandler imageHandler;
    private final Mapper<MenuItem, MenuItemDTO> mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemDTO> get(int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        return menuItemRepository.findAll(pageable).map(mapper::map);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemsByCategoryDTO> getItemsByCategories() {
        return menuItemRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(MenuItem::getCategory))
                .entrySet()
                .stream()
                .map(entry -> new MenuItemsByCategoryDTO(entry.getKey().getName(), mapper.mapList(entry.getValue())))
                .collect(Collectors.toList());
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
                           MultipartFile image) throws IOException {
        MenuItem menuItem = MenuItem.builder()
                .name(name)
                .price(price)
                .category(FoodCategory.valueOf(category))
                .orders(Collections.emptyList())
                .build();
        menuItem = menuItemRepository.save(menuItem);
        String imagePath = imageHandler.save(image, IMAGE_FOLDER, getFilename(menuItem.getId(), image.getOriginalFilename()));
        menuItem.setImage(imagePath);
        return mapper.map(menuItem);
    }

    @Override
    @Transactional
    public MenuItemDTO update(Long id,
                              String name,
                              Double price,
                              String category,
                              MultipartFile image) throws IOException {
        MenuItem menuItem = findMenuItemById(id);
        Optional.ofNullable(name).ifPresent(menuItem::setName);
        Optional.ofNullable(price).ifPresent(menuItem::setPrice);
        Optional.ofNullable(category).map(FoodCategory::valueOf).ifPresent(menuItem::setCategory);
        if (Objects.nonNull(image)) {
            imageHandler.delete(menuItem.getImage());
            String imagePath = imageHandler.save(image, IMAGE_FOLDER, getFilename(menuItem.getId(), image.getOriginalFilename()));
            menuItem.setImage(imagePath);
        }
        return mapper.map(menuItem);
    }

    private String getFilename(Long id, String filename) {
        String extensions = filename.substring(filename.lastIndexOf("."));
        return id.toString().concat(extensions);
    }

    @Override
    @Transactional
    public void remove(Long id) throws IOException {
        MenuItem menuItem = findMenuItemById(id);
        imageHandler.delete(menuItem.getImage());
        menuItemRepository.deleteById(id);
    }

    private MenuItem findMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu item with id " + id + " not found"));
    }
}
