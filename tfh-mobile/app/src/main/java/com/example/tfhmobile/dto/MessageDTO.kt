package com.example.tfhmobile.dto

class MessageDTO constructor (
    private val message: String
    ) {
    fun getMessage(): String {
        return message
    }
}