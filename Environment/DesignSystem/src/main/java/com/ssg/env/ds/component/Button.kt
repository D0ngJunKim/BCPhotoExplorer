package com.ssg.env.ds.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ssg.env.ds.R
import com.ssg.env.ds.composite.LocalText
import com.ssg.env.ds.foundation.RadiusToken
import com.ssg.env.ds.foundation.SpaceToken
import com.ssg.env.ds.foundation.SpaceTokenValues
import com.ssg.env.ds.foundation.background
import com.ssg.env.ds.foundation.border
import com.ssg.env.ds.foundation.clip
import com.ssg.env.ds.foundation.padding
import com.ssg.env.ds.util.asSp

sealed class ButtonType(
    internal val size: ButtonSize,
    internal val radius: RadiusToken,
    internal val textSize: Dp,
) {
    data object SM : ButtonType(
        size = ButtonSize.SM,
        radius = RadiusToken.SM,
        textSize = 13.dp
    )

    data object MD : ButtonType(
        size = ButtonSize.MD,
        radius = RadiusToken.SM,
        textSize = 14.dp
    )

    data object LG : ButtonType(
        size = ButtonSize.LG,
        radius = RadiusToken.SM,
        textSize = 14.dp
    )

    data object XL : ButtonType(
        size = ButtonSize.XL,
        radius = RadiusToken.SM,
        textSize = 15.dp
    )
}

@Immutable
data class ButtonColorSet(
    val fillColor: Color = Color.Transparent,
    val outlineColor: Color = Color.Transparent,
    val textColor: Color = Color.Transparent
)

@Immutable
data class ButtonConfig(
    private val type: ButtonType,
    private val radius: Option.Radius,
    internal val normalColorSet: ButtonColorSet,
    internal val disabledColorSet: ButtonColorSet = normalColorSet
) {
    internal val heightDp = type.size.height
    internal val textSizeDp = type.textSize
    internal val radiusToken = radius.getRadius(type)

    object Option {
        sealed class Radius {
            object Rect : Radius()
            object RoundRect : Radius()
            object Oval : Radius();

            fun getRadius(type: ButtonType): RadiusToken = when (this) {
                Rect -> RadiusToken.Zero
                RoundRect -> type.radius
                Oval -> RadiusToken.Circle
            }
        }
    }
}

internal enum class ButtonSize(val height: Dp) {
    SM(28.dp),
    MD(32.dp),
    LG(36.dp),
    XL(40.dp),
}

@Composable
fun Button(
    config: ButtonConfig,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    padding: SpaceTokenValues = SpaceTokenValues(all = SpaceToken.Zero)
) {
    val colorSet = if (enabled) config.normalColorSet else config.disabledColorSet
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = config.heightDp)
            .requiredHeight(config.heightDp)
            .clip(config.radiusToken)
            .background(colorSet.fillColor, config.radiusToken)
            .border(width = 1.dp, color = colorSet.outlineColor, token = config.radiusToken)
            .padding(padding)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        LocalText(
            text = text,
            color = colorSet.textColor,
            fontSize = config.textSizeDp.asSp(),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
private fun Preview() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Button(
            config = ButtonConfig(
                type = ButtonType.SM,
                radius = ButtonConfig.Option.Radius.Rect,
                normalColorSet = ButtonColorSet(
                    fillColor = colorResource(R.color.gray900),
                    textColor = Color.White
                )
            ),
            text = "버튼",
            onClick = {})
        Button(
            config = ButtonConfig(
                type = ButtonType.MD,
                radius = ButtonConfig.Option.Radius.RoundRect,
                normalColorSet = ButtonColorSet(
                    outlineColor = colorResource(R.color.gray900),
                    textColor = colorResource(R.color.gray900)
                )
            ),
            text = "버튼",
            onClick = {})
        Button(
            config = ButtonConfig(
                type = ButtonType.LG,
                radius = ButtonConfig.Option.Radius.Oval,
                normalColorSet = ButtonColorSet(
                    outlineColor = colorResource(R.color.gray900),
                    textColor = colorResource(R.color.gray900)
                )
            ),
            text = "버튼",
            onClick = {})
        Button(
            config = ButtonConfig(
                type = ButtonType.XL,
                radius = ButtonConfig.Option.Radius.Rect,
                normalColorSet = ButtonColorSet(
                    outlineColor = colorResource(R.color.gray900),
                    textColor = colorResource(R.color.gray900)
                )
            ),
            text = "버튼",
            onClick = {})

    }
}
