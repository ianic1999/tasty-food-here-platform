package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.BookingDTO;
import com.example.tfhbackend.dto.request.BookingRequest;
import com.example.tfhbackend.mapper.Mapper;
import com.example.tfhbackend.model.Booking;
import com.example.tfhbackend.model.Table;
import com.example.tfhbackend.model.exception.EntityNotFoundException;
import com.example.tfhbackend.repository.BookingRepository;
import com.example.tfhbackend.repository.TableRepository;
import com.example.tfhbackend.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final TableRepository tableRepository;
    private final Mapper<Booking, BookingDTO> mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDTO> get(int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        return bookingRepository.findAll(pageable).map(mapper::map);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDTO getById(Long id) {
        return mapper.map(findBookingById(id));
    }

    @Override
    @Transactional
    public BookingDTO add(BookingRequest request) {
        Table table = findTableById(request.getTableId());
        Booking booking = Booking.builder()
                .time(request.getTime())
                .duration(request.getDuration())
                .orders(new ArrayList<>())
                .build();
        booking = bookingRepository.save(booking);
        table.addBooking(booking);
        return mapper.map(booking);
    }

    @Override
    @Transactional
    public BookingDTO update(BookingRequest request) {
        Booking booking = findBookingById(request.getId());
        Table table = findTableById(request.getTableId());
        if (!table.equals(booking.getTable())) {
            Table actualTable = booking.getTable();
            actualTable.removeBooking(booking);
            table.addBooking(booking);
        }
        booking.setTime(request.getTime());
        booking.setDuration(request.getDuration());
        return mapper.map(booking);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        Booking booking = findBookingById(id);
        Table table = booking.getTable();
        table.removeBooking(booking);
        bookingRepository.deleteById(id);
    }

    private Booking findBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id " + id + " not found"));
    }

    private Table findTableById(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Table with id " + id + " not found"));
    }
}
