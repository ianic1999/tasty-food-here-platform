package com.example.tfhmobile.dto

data class LoginRequest (
    private val phone: String,
    private val password: String,
    private val deviceId: String?
)