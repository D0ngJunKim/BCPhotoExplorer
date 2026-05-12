package com.bc.core.presentation.util

import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.Color as ComposeColor

fun ComposeColor.toHexString(): String {
    return "#%06X".format(0xFFFFFF and toArgb())
}
