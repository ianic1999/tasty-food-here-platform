package com.example.tfhmobile.dto

class MenuItemWithQuantityRequest(private var menuItemId: Long,
                                  private val menuItemName: String,
                                  private var quantity: Int) {
    fun increase() {
        quantity += 1;
    }

    fun decrease() {
        quantity -= 1;
    }

    fun getQuantity(): Int {
        return quantity;
    }

    fun getMenuItemId(): Long {
        return menuItemId;
    }

    fun getMenuItemName(): String {
        return menuItemName;
    }
}