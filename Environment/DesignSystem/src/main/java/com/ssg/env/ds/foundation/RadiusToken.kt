package com.ssg.env.ds.foundation

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class RadiusToken(internal val shape: RoundedCornerShape) {
    Zero(RoundedCornerShape(0.dp)),
    SM(RoundedCornerShape(4.dp)),
    MD(RoundedCornerShape(8.dp)),
    LG(RoundedCornerShape(12.dp)),
    XL(RoundedCornerShape(16.dp)),
    Circle(CircleShape);
}


@Stable
fun Modifier.clip(token: RadiusToken) = this
    .clip(token.shape)

@Stable
fun Modifier.background(
    color: Color,
    token: RadiusToken
) = this
    .background(
        color = color,
        shape = token.shape
    )

@Stable
fun Modifier.background(
    brush: Brush,
    token: RadiusToken,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
) = this
    .background(
        brush = brush,
        shape = token.shape,
        alpha = alpha
    )

@Stable
fun Modifier.border(
    width: Dp,
    color: Color,
    token: RadiusToken
) = this
    .border(
        width = width,
        color = color,
        shape = token.shape
    )