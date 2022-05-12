package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.BookingDTO;
import com.example.tfhbackend.dto.request.BookingConfirmationRequest;
import com.example.tfhbackend.dto.request.BookingRequest;
import com.example.tfhbackend.mapper.Mapper;
import com.example.tfhbackend.model.Booking;
import com.example.tfhbackend.model.Table;
import com.example.tfhbackend.model.User;
import com.example.tfhbackend.model.enums.BookingStatus;
import com.example.tfhbackend.model.exception.CustomRuntimeException;
import com.example.tfhbackend.model.exception.EntityNotFoundException;
import com.example.tfhbackend.repository.BookingRepository;
import com.example.tfhbackend.repository.TableRepository;
import com.example.tfhbackend.service.BookingService;
import com.example.tfhbackend.service.SmsService;
import com.example.tfhbackend.util.BookingIdGenerator;
import com.example.tfhbackend.validator.BookingTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class BookingServiceImpl implements BookingService {
    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private static final String TIME_FORMAT = "HH:mm";

    private final BookingRepository bookingRepository;
    private final TableRepository tableRepository;
    private final BookingIdGenerator idGenerator;
    private final SmsService smsService;
    private final BookingTimeValidator bookingTimeValidator;
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
        LocalDateTime time = LocalDate.parse(request.getDate(), DateTimeFormatter.ofPattern(DATE_FORMAT))
                .atTime(LocalTime.parse(request.getTime(), DateTimeFormatter.ofPattern(TIME_FORMAT)));
        bookingTimeValidator.validate(time, time.plusMinutes(request.getDuration()));

        Booking booking = Booking.builder()
                .phone(request.getPhone())
                .clientFullName(request.getFullName())
                .time(time)
                .duration(request.getDuration())
                .confirmed(false)
                .orders(Collections.emptyList())
                .build();
        booking = bookingRepository.save(booking);
        String referenceId = idGenerator.generate(booking.getId());
        booking.setReferenceId(referenceId);
        table.addBooking(booking);
        smsService.sendConfirmationSms(referenceId, request.getPhone(), request.getFullName());
        return mapper.map(booking);
    }

    @Override
    @Transactional
    public BookingDTO update(BookingRequest request) {
        Booking booking = findBookingById(request.getId());
        Table table = findTableById(request.getTableId());
        LocalDateTime time = LocalDate.parse(request.getDate(), DateTimeFormatter.ofPattern(DATE_FORMAT))
                                      .atTime(LocalTime.parse(request.getTime(), DateTimeFormatter.ofPattern(TIME_FORMAT)));
        if (!table.equals(booking.getTable())) {
            Table actualTable = booking.getTable();
            actualTable.removeBooking(booking);
            table.addBooking(booking);
        }
        booking.setTime(time);
        booking.setDuration(request.getDuration());
        return mapper.map(booking);
    }

    @Override
    @Transactional
    public void confirm(BookingConfirmationRequest confirmationRequest) {
        Booking booking = findBookingById(confirmationRequest.getBookingId());
        if (!booking.getReferenceId().equals(confirmationRequest.getReferenceId()))
            throw new CustomRuntimeException("Code is invalid. Please try again");
        booking.setConfirmed(true);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        Booking booking = findBookingById(id);
        Table table = booking.getTable();
        table.removeBooking(booking);
        bookingRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookingDTO close(Long id) {
        Booking booking = findBookingById(id);
        booking.setStatus(BookingStatus.CLOSED);
        booking.getTable().setWaiter(null);
        setWaiterForUpcomingBooking(booking);
        return mapper.map(booking);
    }

    private void setWaiterForUpcomingBooking(Booking booking) {
        Table table = booking.getTable();
        bookingRepository.findActiveForTable(table.getId())
                .ifPresent(b -> table.setWaiter(b.getWaiter()));
    }

    private Booking findBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id " + id + " not found"));
    }

    private Table findTableById(Long id) {
        return tableRepository.findByOrdinalNumber((int) (long) id)
                .orElseThrow(() -> new EntityNotFoundException("Table with id " + id + " not found"));
    }
}
