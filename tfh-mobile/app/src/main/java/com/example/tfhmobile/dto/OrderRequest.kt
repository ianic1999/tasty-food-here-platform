package com.example.tfhmobile.dto

import android.os.Build
import androidx.annotation.RequiresApi

class OrderRequest(private var bookingId: Long) {
    private val items: MutableList<MenuItemWithQuantityRequest> = mutableListOf()

    fun increase(itemId: Long) {
        for (item in items) {
            if (item.getMenuItemId() == itemId) {
                item.increase()
            }
        }
    }

    fun decrease(itemId: Long) {
        for (item in items) {
            if (item.getMenuItemId() == itemId) {
                item.decrease()
            }
        }
    }

    fun addItem(item: MenuItemWithQuantityRequest) {
        items.add(item)
    }

    fun getItems(): MutableList<MenuItemWithQuantityRequest> {
        return items;
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun removeItem(itemId: Long) {
        items.removeIf { it.getMenuItemId() == itemId }
    }
}