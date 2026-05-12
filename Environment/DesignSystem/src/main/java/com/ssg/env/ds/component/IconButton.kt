package com.ssg.env.ds.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ssg.env.ds.R
import com.ssg.env.ds.composite.LocalImage
import com.ssg.env.ds.foundation.RadiusToken
import com.ssg.env.ds.foundation.ShadowToken
import com.ssg.env.ds.foundation.SpaceToken
import com.ssg.env.ds.foundation.background
import com.ssg.env.ds.foundation.border
import com.ssg.env.ds.foundation.clip
import com.ssg.env.ds.foundation.padding
import com.ssg.env.ds.foundation.shadow

sealed class IconButtonType(
    internal val size: IconButtonSize,
    internal val radius: RadiusToken,
    internal val iconSize: Dp
) {
    data object SM : IconButtonType(
        size = IconButtonSize.SM,
        radius = RadiusToken.SM,
        iconSize = 16.dp
    )

    data object MD : IconButtonType(
        size = IconButtonSize.MD,
        radius = RadiusToken.SM,
        iconSize = 20.dp
    )

    data object LG : IconButtonType(
        size = IconButtonSize.LG,
        radius = RadiusToken.SM,
        iconSize = 20.dp
    )

    data object XL : IconButtonType(
        size = IconButtonSize.XL,
        radius = RadiusToken.SM,
        iconSize = 24.dp
    )

    data object XXL : IconButtonType(
        size = IconButtonSize.XXL,
        radius = RadiusToken.SM,
        iconSize = 24.dp
    )
}

@Immutable
data class IconButtonColorSet(
    val fillColor: Color = Color.Transparent,
    val outlineColor: Color = Color.Transparent,
    val iconColor: Color? = null
)

@Immutable
data class IconButtonConfig(
    private val type: IconButtonType,
    private val radius: Option.Radius,
    internal val normalColorSet: IconButtonColorSet,
    internal val selectedColorSet: IconButtonColorSet = normalColorSet,
    internal val disabledColorSet: IconButtonColorSet = normalColorSet,
    internal val shadowToken: ShadowToken? = null
) {
    internal val heightDp = type.size.height
    internal val iconSizeDp = type.iconSize
    internal val radiusToken = radius.getRadius(type)

    object Option {
        sealed class Radius {
            object Rect : Radius()
            object RoundRect : Radius()
            object Oval : Radius();

            fun getRadius(type: IconButtonType): RadiusToken = when (this) {
                Rect -> RadiusToken.Zero
                RoundRect -> type.radius
                Oval -> RadiusToken.Circle
            }
        }
    }
}

internal enum class IconButtonSize(val height: Dp) {
    SM(28.dp),
    MD(32.dp),
    LG(36.dp),
    XL(40.dp),
    XXL(48.dp)
}

@Composable
fun IconButton(
    config: IconButtonConfig,
    painter: Painter,
    onClick: () -> Unit,
    buttonDescription: String,
    modifier: Modifier = Modifier,
    padding: SpaceToken = SpaceToken.Zero,
    enabled: Boolean = true,
    selected: Boolean = false,
) {
    val colorSet = if (enabled) {
        if (selected) config.selectedColorSet else config.normalColorSet
    } else {
        config.disabledColorSet
    }
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = config.heightDp)
            .requiredHeight(config.heightDp)
            .then(
                if (config.shadowToken != null) {
                    Modifier.shadow(config.shadowToken, config.radiusToken)
                } else {
                    Modifier
                }
            )
            .background(colorSet.fillColor, config.radiusToken)
            .border(width = 1.dp, color = colorSet.outlineColor, token = config.radiusToken)
            .clip(config.radiusToken)
            .padding(horizontal = padding)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .semantics(mergeDescendants = true) {
                contentDescription = buttonDescription
            },
        contentAlignment = Alignment.Center
    ) {
        LocalImage(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(config.iconSizeDp),
            colorFilter = colorSet.iconColor?.let { ColorFilter.tint(it) }
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
        IconButton(
            config = IconButtonConfig(
                type = IconButtonType.SM,
                radius = IconButtonConfig.Option.Radius.Rect,
                normalColorSet = IconButtonColorSet(
                    fillColor = colorResource(R.color.gray900),
                    iconColor = Color.White
                )
            ),
            painter = painterResource(R.drawable.ico_heart),
            onClick = {},
            buttonDescription = "좋아요"
        )
        IconButton(
            config = IconButtonConfig(
                type = IconButtonType.MD,
                radius = IconButtonConfig.Option.Radius.RoundRect,
                normalColorSet = IconButtonColorSet(
                    outlineColor = colorResource(R.color.gray900),
                )
            ),
            painter = painterResource(R.drawable.ico_heart),
            onClick = {},
            buttonDescription = "좋아요"
        )
        IconButton(
            config = IconButtonConfig(
                type = IconButtonType.LG,
                radius = IconButtonConfig.Option.Radius.Oval,
                normalColorSet = IconButtonColorSet(
                    outlineColor = colorResource(R.color.gray900),
                )
            ),
            painter = painterResource(R.drawable.ico_heart),
            onClick = {},
            buttonDescription = "좋아요",
            padding = SpaceToken.XXXS
        )
        IconButton(
            config = IconButtonConfig(
                type = IconButtonType.XL,
                radius = IconButtonConfig.Option.Radius.Rect,
                normalColorSet = IconButtonColorSet(
                    outlineColor = colorResource(R.color.gray900),
                    iconColor = colorResource(R.color.gray900)
                )
            ),
            painter = painterResource(R.drawable.ico_heart),
            onClick = {},
            buttonDescription = "좋아요"
        )
    }
}
