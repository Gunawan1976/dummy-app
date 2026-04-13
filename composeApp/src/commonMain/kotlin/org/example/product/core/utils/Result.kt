package org.example.product.core.utils

sealed class Results<out T> {
    data class Success<out T>(val data: T) : Results<T>()
    data class Error(val message: String, val exception: Throwable? = null) : Results<Nothing>()
    object Loading : Results<Nothing>()
}