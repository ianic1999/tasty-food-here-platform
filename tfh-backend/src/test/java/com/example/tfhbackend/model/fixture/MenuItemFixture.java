package com.example.tfhbackend.model.fixture;

import com.example.tfhbackend.model.MenuItem;
import com.example.tfhbackend.model.enums.FoodCategory;

public class MenuItemFixture {

    public static MenuItem cocaCola() {
        return MenuItem.builder()
                .price(20.0)
                .category(FoodCategory.BEVERAGE)
                .name("Coca-Cola")
                .image("coca-cola.png")
                .build();
    }

    public static MenuItem coffee() {
        return MenuItem.builder()
                .price(30.0)
                .category(FoodCategory.HOT_DRINKS)
                .name("Coffee")
                .image("coffee.png")
                .build();
    }

    public static MenuItem pizza() {
        return MenuItem.builder()
                .price(80.0)
                .category(FoodCategory.FAST_FOOD)
                .name("Pizza")
                .image("pizza.png")
                .build();
    }
}
