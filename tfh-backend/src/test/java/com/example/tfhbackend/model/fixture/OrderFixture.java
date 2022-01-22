package com.example.tfhbackend.model.fixture;

import com.example.tfhbackend.model.Booking;
import com.example.tfhbackend.model.Order;

import java.util.List;

public class OrderFixture {

    public static Order forBooking(Booking booking) {
        var order = Order.builder()
                .booking(booking)
                .items(List.of(
                        MenuItemFixture.coffee(),
                        MenuItemFixture.cocaCola()
                ))
                .build();
        booking.addOrder(order);
        return order;
    }
}
