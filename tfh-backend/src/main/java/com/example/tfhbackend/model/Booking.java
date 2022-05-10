package com.example.tfhbackend.model;

import com.example.tfhbackend.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "Reference ID should be provided")
    @Getter
    private String referenceId;

    @NotNull(message = "Booking time should be provided")
    private LocalDateTime time;

    @Getter
    @Setter
    private Boolean confirmed;

    private Integer duration;

    @NotEmpty(message = "Client phone should be provided")
    private String phone;

    @NotEmpty(message = "Client full name should be provided")
    private String clientFullName;

    @NotNull(message = "Booking table should be provided")
    @ManyToOne(fetch = FetchType.LAZY)
    private Table table;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @Getter
    @Setter
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.UPCOMING;

    @ManyToOne
    @Getter
    @Setter
    private User waiter;

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
