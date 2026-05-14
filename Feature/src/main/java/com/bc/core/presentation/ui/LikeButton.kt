package com.bc.core.presentation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bc.feature.R
import com.ssg.env.ds.component.IconButton
import com.ssg.env.ds.component.IconButtonColorSet
import com.ssg.env.ds.component.IconButtonConfig
import com.ssg.env.ds.component.IconButtonType
import com.ssg.env.ds.composite.LocalCircularProgressIndicator
import com.ssg.env.ds.foundation.RadiusToken
import com.ssg.env.ds.foundation.background

@Composable
fun LikeButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: IconButtonType = IconButtonType.MD,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val currentSelected by rememberUpdatedState(selected)
    val scale = remember { Animatable(1f) }
    var isLoading by remember { mutableStateOf(false) }
    var previousSelected by remember { mutableStateOf(selected) }
    var hasPendingUserClick by remember { mutableStateOf(false) }

    LaunchedEffect(selected) {
        if (previousSelected != selected) {
            previousSelected = selected
            isLoading = false

            if (hasPendingUserClick) {
                hasPendingUserClick = false
                hapticFeedback.performHapticFeedback(if (selected) HapticFeedbackType.ToggleOn else HapticFeedbackType.ToggleOff)
                scale.snapTo(
                    targetValue = 0.8f
                )
                scale.animateTo(
                    targetValue = 1f, animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            }
        }
    }

    Box(
        modifier = modifier
            .background(
                color = Color.White,
                token = RadiusToken.Circle
            ),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            config = IconButtonConfig(
                type = type,
                radius = IconButtonConfig.Option.Radius.Oval,
                normalColorSet = IconButtonColorSet(
                    iconColor = if (isLoading) Color.Transparent else colorResource(R.color.gray900)
                ),
                selectedColorSet = IconButtonColorSet(
                    iconColor = if (isLoading) Color.Transparent else colorResource(R.color.primary)
                )
            ),
            painter = painterResource(R.drawable.ico_heart),
            onClick = {
                hasPendingUserClick = true
                isLoading = true
                onClick()
            },
            buttonDescription = "좋아요",
            enabled = !isLoading,
            selected = currentSelected,
            modifier = Modifier
                .scale(scale.value)
        )

        if (isLoading) {
            LocalCircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = colorResource(R.color.primary),
                strokeWidth = 2.dp
            )
        }
    }
}
