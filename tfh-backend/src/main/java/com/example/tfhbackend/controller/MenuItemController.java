package com.example.tfhbackend.controller;

import com.example.tfhbackend.dto.MenuItemDTO;
import com.example.tfhbackend.dto.response.PaginatedResponse;
import com.example.tfhbackend.dto.response.Response;
import com.example.tfhbackend.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/menu_items")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class MenuItemController {
    private final MenuItemService menuItemService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<MenuItemDTO>> get(@RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "15") int perPage) {
        log.info("/api/menu_items: GET request with parameters: page: {}, perPage: {}", page, perPage);
        var response = menuItemService.get(page, perPage);
        log.info("/api/menu_items: Response status: {}", HttpStatus.OK);
        return ResponseEntity.ok(
                new PaginatedResponse<>(response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<MenuItemDTO>> getById(@PathVariable Long id) {
        log.info("/api/menu_items/{id}: GET request with parameters: id: {}", id);
        var response = menuItemService.getById(id);
        log.info("/api/menu_items/{id}: Response Status: {}", HttpStatus.OK);
        return ResponseEntity.ok(
                new Response<>(response)
        );
    }

    @PostMapping
    public ResponseEntity<Response<MenuItemDTO>> add(@RequestParam String name,
                                                     @RequestParam Double price,
                                                     @RequestParam String category,
                                                     @RequestParam MultipartFile image) {
        log.info("/api/menu_items: POST request for adding item with name: {}", name);
        var response = menuItemService.add(name, price, category, image);
        log.info("/api/menu_items: Menu items added, id={}, Response status: {}", response.getId(), HttpStatus.CREATED);
        return new ResponseEntity<>(
                new Response<>(response),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response<MenuItemDTO>> update(@PathVariable Long id,
                                                        @RequestParam(required = false) String name,
                                                        @RequestParam(required = false) Double price,
                                                        @RequestParam(required = false) String category,
                                                        @RequestParam(required = false) MultipartFile image) {
        log.info("/api/menu_items/{id}: PATCH request for updating item with id: {}", id);
        var response = menuItemService.update(id, name, price, category, image);
        log.info("/api/menu_items/{id}: Menu items updated, Response status: {}", HttpStatus.OK);
        return ResponseEntity.ok(
                new Response<>(response)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id) {
        log.info("/api/menu_items/{id}: DELETE request for removing item with id: {}", id);
        menuItemService.remove(id);
        log.info("/api/menu_items/{id}: Item with id {} remove, Response status={}", id, HttpStatus.NO_CONTENT);
        return ResponseEntity.noContent().build();
    }
}
