package com.example.tfhmobile.util

class ConnectionModel(private val type: Int,
                      private val connected: Boolean) {

    fun getType(): Int {
        return type
    }

    fun isConnected(): Boolean {
        return connected
    }
}