package com.example.tfhmobile.dto

class OrderDTO constructor(
    private val id: Long,
    private val bookingId: Long,
    private val items: List<MenuItemWithCountDTO>
) {

    fun getId(): Long {
        return id
    }

    fun getItems(): List<MenuItemWithCountDTO> {
        return items
    }

    fun getBookingId(): Long = bookingId;
}