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
        log.info("/api/orders: GET request with parameters: page: {}, perPage: {}", page, perPage);
        var response = orderService.get(page, perPage);
        log.info("/api/orders: Response status: {}", HttpStatus.OK);
        return ResponseEntity.ok(
                new PaginatedResponse<>(response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<OrderDTO>> getById(@PathVariable Long id) {
        log.info("/api/orders/{id}: GET request with parameters: id: {}", id);
        var response = orderService.getById(id);
        log.info("/api/orders/{id}: Response Status: {}", HttpStatus.OK);
        return ResponseEntity.ok(
                new Response<>(response)
        );
    }

    @PostMapping
    public ResponseEntity<Response<OrderDTO>> add(@RequestBody OrderRequest request) {
        log.info("/api/orders: POST request for adding order for booking: {}", request.getBookingId());
        var response = orderService.add(request);
        log.info("/api/orders: Order added, id={}, Response status: {}", response.getId(), HttpStatus.CREATED);
        return new ResponseEntity<>(
                new Response<>(response),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id) {
        log.info("/api/orders/{id}: DELETE request for removing order with id: {}", id);
        orderService.remove(id);
        log.info("/api/orders/{id}: Order with id {} removed, Response status={}", id, HttpStatus.NO_CONTENT);
        return ResponseEntity.noContent().build();
    }
}
