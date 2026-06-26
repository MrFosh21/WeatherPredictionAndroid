package com.example.skycast.core.result

sealed interface Resource<out T> {
    data class Success<T>(val data: T, val fromCache: Boolean = false) : Resource<T>
    data class Error(val message: String, val cause: Throwable? = null) : Resource<Nothing>
    data object Loading : Resource<Nothing>
}
