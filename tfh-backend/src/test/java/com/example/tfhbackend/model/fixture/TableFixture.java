package com.example.tfhbackend.model.fixture;

import com.example.tfhbackend.model.Table;

public class TableFixture {
    private Long id;
    private static int ordinalNumber = 10001;
    private static int nrOfSpots = 10;

    public static Table aTable() {
        return Table.builder()
                .ordinalNumber(ordinalNumber)
                .nrOfSpots(nrOfSpots)
                .build();
    }
}
