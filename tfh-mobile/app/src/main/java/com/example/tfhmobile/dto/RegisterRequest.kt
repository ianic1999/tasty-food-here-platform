package com.example.tfhmobile.dto

data class RegisterRequest (
    private val firstName: String,
    private val lastName: String,
    private val phone: String,
    private val email: String,
    private val password: String)