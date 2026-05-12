package com.ssg.env.ds.foundation

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

enum class ShadowToken(private val shadowFactory: (() -> Array<Shadow>)) {
    MD(shadowFactory = {
        arrayOf(
            Shadow(
                offset = DpOffset(0.dp, 4.dp),
                radius = 6.dp,
                spread = (-1).dp,
                color = Color.Black.copy(alpha = 0.1f)
            ),
            Shadow(
                offset = DpOffset(0.dp, 2.dp),
                radius = 4.dp,
                spread = (-1).dp,
                color = Color.Black.copy(alpha = 0.06f)
            )
        )
    });

    internal val shadows: Array<Shadow>
        get() = shadowFactory.invoke()
}


fun Modifier.shadow(shadow: ShadowToken, radius: RadiusToken): Modifier {
    var modifier = this
    shadow.shadows.forEach { shadow ->
        modifier = modifier.dropShadow(radius.shape, shadow)
    }
    return modifier
}