package com.example.tfhbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@javax.persistence.Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "incrementDomain")
    @GenericGenerator(name = "incrementDomain", strategy = "increment")
    private Long id;

    @NotNull(message = "Booking time should be provided")
    private LocalDateTime time;

    private Integer duration;

    @NotNull(message = "Booking table should be provided")
    @ManyToOne(fetch = FetchType.LAZY)
    private Table table;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id.equals(booking.id);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setBooking(this);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        order.setBooking(null);
    }
}
