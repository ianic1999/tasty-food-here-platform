package com.example.tfhbackend.model;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
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
}
