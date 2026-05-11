package com.ssg.env.ds.composite

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.layout.Layout
import com.ssg.env.ds.foundation.SpaceToken

@Composable
fun LocalExpandLayout(
    modifier: Modifier,
    edgeSpaceToken: SpaceToken,
    content: @Composable @UiComposable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeable = measurables[0].measure(
            constraints.copy(
                minWidth = 0,
                minHeight = 0,
                maxWidth = constraints.maxWidth + (edgeSpaceToken.size * 2).roundToPx()
            )
        )
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}