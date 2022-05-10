package com.example.tfhbackend.controller;

import com.example.tfhbackend.dto.OrderDTO;
import com.example.tfhbackend.dto.request.OrderRequest;
import com.example.tfhbackend.dto.response.PaginatedResponse;
import com.example.tfhbackend.dto.response.Response;
import com.example.tfhbackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<OrderDTO>> get(@RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(defaultValue = "15") int perPage) {
        var response = orderService.get(page, perPage);
        return ResponseEntity.ok(
                new PaginatedResponse<>(response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<OrderDTO>> getById(@PathVariable Long id) {
        var response = orderService.getById(id);
        return ResponseEntity.ok(
                new Response<>(response)
        );
    }

    @PostMapping
    public ResponseEntity<Response<OrderDTO>> add(@RequestBody OrderRequest request) {
        var response = orderService.add(request);
        return new ResponseEntity<>(
                new Response<>(response),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        orderService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
