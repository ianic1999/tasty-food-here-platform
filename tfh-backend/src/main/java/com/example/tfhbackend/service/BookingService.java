package com.example.tfhbackend.service;

import com.example.tfhbackend.dto.BookingDTO;
import com.example.tfhbackend.dto.request.BookingConfirmationRequest;
import com.example.tfhbackend.dto.request.BookingRequest;
import org.springframework.data.domain.Page;

public interface BookingService {
    Page<BookingDTO> get(int page, int perPage);
    BookingDTO getById(Long id);
    BookingDTO add(BookingRequest request);
    BookingDTO update(BookingRequest request);
    void confirm(BookingConfirmationRequest confirmationRequest);
    void remove(Long id);
    BookingDTO close(Long id);
}
