package com.example.tfhbackend.controller;

import com.example.tfhbackend.dto.BookingDTO;
import com.example.tfhbackend.dto.FeedbackDTO;
import com.example.tfhbackend.dto.request.BookingRequest;
import com.example.tfhbackend.dto.response.PaginatedResponse;
import com.example.tfhbackend.dto.response.Response;
import com.example.tfhbackend.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<BookingDTO>> get(@RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int perPage) {
        log.info("/api/bookings: GET request with parameters: page: {}, perPage: {} ", page, perPage);
        var response = bookingService.get(page, perPage);
        log.info("/api/bookings: Response status: {}", HttpStatus.OK);
        return ResponseEntity.ok(
                new PaginatedResponse<>(response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<BookingDTO>> getById(@PathVariable Long id) {
        log.info("/api/bookings/{id}: GET request with parameters: id: {}", id);
        var response = bookingService.getById(id);
        log.info("/api/bookings/{id}: Response Status: {}", HttpStatus.OK);
        return ResponseEntity.ok(
                new Response<>(response)
        );
    }

    @PostMapping
    public ResponseEntity<Response<BookingDTO>> add(@RequestBody BookingRequest request) {
        log.info("/api/bookings: POST request with parameters: time: {}, duration: {}, table: {}", request.getTime(), request.getDuration(), request.getTableId());
        var response = bookingService.add(request);
        log.info("/api/bookings: Booking added, id={}, Response status: {}", response.getId(), HttpStatus.CREATED);
        return new ResponseEntity<>(
                new Response<>(response),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<BookingDTO>> update(@PathVariable Long id,
                                                       @RequestBody BookingRequest request) {
        log.info("/api/bookings/{id}: PUT request for updating booking with id: {}", id);
        request.setId(id);
        var response = bookingService.update(request);
        log.info("/api/bookings/{id}: Booking updated, Response status: {}", HttpStatus.OK);
        return ResponseEntity.ok(
                new Response<>(response)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        log.info("/api/bookings/{id}: DELETE request for removing booking with id: {}", id);
        bookingService.remove(id);
        log.info("/api/bookings/{id}: Booking with id {} removed, Response status={}", id, HttpStatus.NO_CONTENT);
        return ResponseEntity.noContent().build();
    }
}
