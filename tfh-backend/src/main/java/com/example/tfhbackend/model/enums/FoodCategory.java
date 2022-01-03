package com.example.tfhbackend.model.enums;

public enum FoodCategory {

    FAST_FOOD("Fast Food"),
    BEVERAGE("Beverage"),
    HOT_DRINKS("Hot drinks");

    private String name;

    FoodCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
