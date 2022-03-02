package com.example.tfhmobile.presentation

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Failure<T>(val error: Throwable, val data: T? = null) : Resource<T>()
    data class Loading<T>(val data: T? = null) : Resource<T>()
}