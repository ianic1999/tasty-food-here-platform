package com.example.tfhbackend.model.enums;

public enum FoodCategory {

    HOT("Hot", 3),
    SOUP("Soups", 2),
    BREAKFAST("Breakfast", 1),
    DESSERT("Desserts", 8),
    SALAD("Salads", 4),
    FAST_FOOD("Fast Food", 5),
    BEVERAGE("Beverage", 9),
    HOT_DRINKS("Hot drinks", 10),
    PIE("Pies", 7),
    SNACKS("Snacks", 6);

    private final String name;
    private final int order;

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
