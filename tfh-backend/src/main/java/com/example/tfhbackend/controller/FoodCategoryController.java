package com.example.tfhbackend.controller;

import com.example.tfhbackend.dto.FoodCategoryDTO;
import com.example.tfhbackend.dto.response.Response;
import com.example.tfhbackend.mapper.Mapper;
import com.example.tfhbackend.model.enums.FoodCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/food_categories")
@CrossOrigin("*")
@RequiredArgsConstructor
public class FoodCategoryController {
    private final Mapper<FoodCategory, FoodCategoryDTO> mapper;

    @GetMapping
    public ResponseEntity<Response<List<FoodCategoryDTO>>> get() {
        return ResponseEntity.ok(
                new Response<>(
                        mapper.mapList(List.of(FoodCategory.values()))
                )
        );
    }
}
