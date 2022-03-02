package com.example.tfhmobile.dto

class MenuItemsByCategoryDTO(private val category: String,
                             private val items: List<MenuItemDTO>) {
    fun getCategory(): String {
        return category
    }

    fun getItems(): List<MenuItemDTO> {
        return items
    }
}