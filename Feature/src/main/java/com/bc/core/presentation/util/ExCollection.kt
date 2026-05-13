package com.bc.core.presentation.util

fun <T> MutableList<T>.addNotNull(value: T?): Boolean {
    if (value == null) return false
    return add(value)
}