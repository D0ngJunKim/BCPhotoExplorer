package com.ssg.env.ds.composite

import androidx.annotation.FloatRange
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LocalCircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    strokeWidth: Dp = 2.dp,
    trackColor: Color = Color.Transparent,
) {
    val minSweep = 24f
    val maxSweep = 300f
    val sweepRange = maxSweep - minSweep

    val infiniteTransition = rememberInfiniteTransition(label = "circular-indicator")
    val rotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 444f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "circular-indicator-rotation"
    )
    val headProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1200
                0f at 0 using FastOutSlowInEasing
                1f at 600 using FastOutSlowInEasing
                1f at 1200
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circular-indicator-head"
    )
    val tailProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1200
                0f at 0
                0f at 600 using FastOutSlowInEasing
                1f at 1200 using FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circular-indicator-tail"
    )

    CircularProgressIndicatorCanvas(
        modifier = modifier,
        strokeWidth = strokeWidth,
        trackColor = trackColor,
    ) { stroke, arcTopLeft, arcSize ->
        val headAngle = minSweep + sweepRange * headProgress.value
        val tailAngle = sweepRange * tailProgress.value
        val sweepAngle = (headAngle - tailAngle).coerceAtLeast(minSweep)
        val startAngle = rotation.value + tailAngle - 90f

        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = arcTopLeft,
            size = arcSize,
            style = stroke
        )
    }
}

@Composable
fun LocalCircularProgressIndicator(
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    strokeWidth: Dp = 2.dp,
    trackColor: Color = Color.Transparent,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(
            durationMillis = 450,
            easing = FastOutSlowInEasing
        ),
        label = "circular-indicator-determinate-progress"
    )

    CircularProgressIndicatorCanvas(
        modifier = modifier,
        strokeWidth = strokeWidth,
        trackColor = trackColor,
    ) { stroke, arcTopLeft, arcSize ->
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = 360f * animatedProgress,
            useCenter = false,
            topLeft = arcTopLeft,
            size = arcSize,
            style = stroke
        )
    }
}

@Composable
private fun CircularProgressIndicatorCanvas(
    modifier: Modifier,
    strokeWidth: Dp,
    trackColor: Color,
    drawIndicator: DrawScope.(Stroke, Offset, Size) -> Unit,
) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        val inset = stroke.width / 2f
        val arcSize = Size(
            width = size.width - inset * 2,
            height = size.height - inset * 2
        )
        val arcTopLeft = Offset(inset, inset)

        if (trackColor.alpha > 0f) {
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = stroke
            )
        }

        drawIndicator(stroke, arcTopLeft, arcSize)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewIndeterminate() {
    LocalCircularProgressIndicator(
        color = Color.Black,
        modifier = Modifier.size(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewDeterminate() {
    var progress by remember { mutableFloatStateOf(0.1f) }

    LocalCircularProgressIndicator(
        progress = progress,
        color = Color.Black,
        modifier = Modifier
            .size(16.dp)
            .clickable {
                progress = (progress + 0.1f).coerceAtMost(1f)
            }
    )
}
