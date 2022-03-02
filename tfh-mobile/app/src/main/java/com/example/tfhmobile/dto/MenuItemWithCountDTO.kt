package com.example.tfhmobile.dto

class MenuItemWithCountDTO constructor(
    private val item: MenuItemDTO,
    private val count: Int,
    private val price: Double
) {
    fun getItem(): MenuItemDTO {
        return item;
    }

    fun getCount(): Int {
        return count
    }

    fun getPrice(): Double {
        return price
    }
}