package com.example.tfhbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@javax.persistence.Table(name = "tables")
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "incrementDomain")
    @GenericGenerator(name = "incrementDomain", strategy = "increment")
    private Long id;

    private Integer ordinalNumber;

    private Integer nrOfSpots;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "table", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User waiter;

    public Long getId() {
        return id;
    }

    public Integer getOrdinalNumber() {
        return ordinalNumber;
    }

    public void setOrdinalNumber(Integer ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

    public Integer getNrOfSpots() {
        return nrOfSpots;
    }

    public void setNrOfSpots(Integer nrOfSpots) {
        this.nrOfSpots = nrOfSpots;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setTable(this);
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        booking.setTable(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return id.equals(table.id);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
