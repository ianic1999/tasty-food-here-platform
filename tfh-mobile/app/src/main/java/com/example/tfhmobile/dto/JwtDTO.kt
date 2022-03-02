package com.example.tfhmobile.dto

class JwtDTO (
    private val accessToken: String,
    private val refreshToken: String
    ) {
    fun getToken(): String {
        return accessToken
    }
}