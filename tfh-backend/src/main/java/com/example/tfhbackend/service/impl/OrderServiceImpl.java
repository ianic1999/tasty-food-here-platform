package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.OrderDTO;
import com.example.tfhbackend.dto.request.OrderRequest;
import com.example.tfhbackend.mapper.Mapper;
import com.example.tfhbackend.model.Booking;
import com.example.tfhbackend.model.MenuItem;
import com.example.tfhbackend.model.Order;
import com.example.tfhbackend.model.exception.EntityNotFoundException;
import com.example.tfhbackend.repository.BookingRepository;
import com.example.tfhbackend.repository.MenuItemRepository;
import com.example.tfhbackend.repository.OrderRepository;
import com.example.tfhbackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BookingRepository bookingRepository;
    private final MenuItemRepository menuItemRepository;
    private final Mapper<Order, OrderDTO> mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> get(int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        return orderRepository.findAll(pageable).map(mapper::map);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getForTable(Long tableId) {
        return mapper.mapList(orderRepository.findByTableId(tableId));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getById(Long id) {
        return mapper.map(findOrderById(id));
    }

    @Override
    @Transactional
    public OrderDTO add(OrderRequest request) {
        Booking booking = findBookingById(request.getBookingId());
        List<MenuItem> menuItems = request.getMenuItemIds()
                .stream()
                .map(this::findMenuItemById)
                .collect(Collectors.toList());
        Order order = new Order();
        order = orderRepository.save(order);
        booking.addOrder(order);
        menuItems.forEach(order::addMenuItem);
        return mapper.map(order);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        Order order = findOrderById(id);
        order.getItems()
                .forEach(order::removeMenuItem);
        order.removeBooking();
        orderRepository.deleteById(id);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not found"));
    }

    private MenuItem findMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu item with id " + id + " not found"));
    }

    private Booking findBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id " + id + " not found"));
    }
}
