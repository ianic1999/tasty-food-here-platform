package com.example.tfhmobile.dto

class MenuItemDTO constructor(
    private val id: Long,
    private val name: String,
    private val price: Double,
    private val image: String,
    private val category: String
) {
    fun getId(): Long {
        return id
    }

    fun getName(): String {
        return name
    }
}