package com.example.tfhmobile.dto

class HttpResponse<T> constructor(
    private val data: T
    ) {
    fun getData(): T {
        return data
    }
}