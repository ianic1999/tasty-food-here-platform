package com.example.tfhbackend.model.enums;

public enum FoodCategory {

    HOT("Hot", 3),
    SOUP("Soups", 2),
    BREAKFAST("Breakfast", 1),
    DESSERT("Desserts", 6),
    SALAD("Salads", 4),
    FAST_FOOD("Fast Food", 5),
    BEVERAGE("Beverage", 7),
    HOT_DRINKS("Hot drinks", 8);

    private String name;
    private int order;

    FoodCategory(String name,
                 int order) {
        this.name = name;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }
}
