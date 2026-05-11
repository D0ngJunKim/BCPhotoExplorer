package com.bc.core.presentation.util

import androidx.core.graphics.toColorInt
import androidx.compose.ui.graphics.Color as ComposeColor

fun String?.toComposeColorOrNull(): ComposeColor? {
    if (this.isNullOrEmpty()) return null
    return try {
        ComposeColor(this.toColorInt())
    } catch (_: Exception) {
        null
    }
}