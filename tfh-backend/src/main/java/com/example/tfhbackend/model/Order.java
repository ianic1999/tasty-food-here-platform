package com.example.tfhbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "incrementDomain")
    @GenericGenerator(name = "incrementDomain", strategy = "increment")
    private Long id;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_item",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id", referencedColumnName = "id")
    )
    private List<MenuItem> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Booking booking;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    public Long getId() {
        return id;
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public void addMenuItem(MenuItem menuItem) {
        items.add(menuItem);
        menuItem.addOrder(this);
    }

    public void removeBooking() {
        booking.getOrders().remove(this);
        setBooking(null);
    }

    public void removeMenuItem(MenuItem menuItem) {
        items.remove(menuItem);
        menuItem.removeOrder(this);
    }
}
