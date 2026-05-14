package com.bc.feature.viewer.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil3.BitmapImage
import coil3.SingletonImageLoader
import coil3.memory.MemoryCache
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.toBitmap
import com.bc.core.domain.model.PhotoItemModel
import com.bc.env.nav.IRoute
import com.bc.env.nav.IRouteConfig
import com.bc.env.nav.NavTransition
import com.bc.env.nav.annotation.OverlayContainer
import kotlinx.serialization.Serializable
import coil3.Image as CoilImage

@Serializable
@OverlayContainer
data class PhotoViewerRoute(
    val data: PhotoItemModel
) : IRoute.Screen {
    companion object : IRouteConfig.Screen {
        override val transition: NavTransition = NavTransition.Immediate
    }

    @Composable
    override fun Content() {
        PhotoViewerScreen(photo = data)
    }
}

@Composable
fun PhotoViewerScreen(
    photo: PhotoItemModel
) {
    var enterStarted by rememberSaveable(photo.id) { mutableStateOf(false) }
    val enterProgress by animateFloatAsState(
        targetValue = if (enterStarted) 1f else 0f,
        animationSpec = tween(
            durationMillis = 320,
            easing = FastOutSlowInEasing
        )
    )

    LaunchedEffect(photo.id) {
        enterStarted = true
    }

    PhotoViewerContent(
        photo = photo,
        enterProgress = enterProgress
    )
}

@Composable
private fun PhotoViewerContent(
    photo: PhotoItemModel,
    enterProgress: Float
) {
    val ratio = photo.ratio()
    val imageBitmap by rememberCoilImageBitmap(photo.imageUrl)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = enterProgress))
    ) {
        val density = LocalDensity.current
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val screenRatio = screenWidth / screenHeight
        val targetWidth: Dp
        val targetHeight: Dp

        if (screenRatio > ratio) {
            targetHeight = screenHeight
            targetWidth = screenHeight * ratio
        } else {
            targetWidth = screenWidth
            targetHeight = screenWidth / ratio
        }

        val startWidth = screenWidth
        val startHeight = screenWidth / ratio
        val imageWidth = startWidth.lerpTo(targetWidth, enterProgress)
        val imageHeight = startHeight.lerpTo(targetHeight, enterProgress)
        val imageX = 0.dp.lerpTo((screenWidth - targetWidth) / 2f, enterProgress)
        val imageY = 0.dp.lerpTo((screenHeight - targetHeight) / 2f, enterProgress)
        val targetWidthPx = with(density) { targetWidth.toPx() }
        val targetHeightPx = with(density) { targetHeight.toPx() }
        val screenWidthPx = with(density) { screenWidth.toPx() }
        val screenHeightPx = with(density) { screenHeight.toPx() }

        var userScale by rememberSaveable(photo.id) { mutableFloatStateOf(1f) }
        var userOffset by rememberSaveable(photo.id, stateSaver = OffsetSaver) {
            mutableStateOf(Offset.Zero)
        }
        val gesturesEnabled = enterProgress >= 0.98f

        imageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap,
                contentDescription = photo.altDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = imageX.roundToPx(),
                            y = imageY.roundToPx()
                        )
                    }
                    .size(imageWidth, imageHeight)
                    .graphicsLayer {
                        val activeScale = if (gesturesEnabled) userScale else 1f
                        scaleX = activeScale
                        scaleY = activeScale
                        translationX = if (gesturesEnabled) userOffset.x else 0f
                        translationY = if (gesturesEnabled) userOffset.y else 0f
                    }
                    .pointerInput(gesturesEnabled, targetWidthPx, targetHeightPx) {
                        if (!gesturesEnabled) return@pointerInput

                        detectTransformGestures { _, pan, zoom, _ ->
                            val nextScale = (userScale * zoom).coerceIn(1f, MaxZoomScale)
                            userScale = nextScale

                            if (nextScale == 1f) {
                                userOffset = Offset.Zero
                                return@detectTransformGestures
                            }

                            val maxPanX = ((targetWidthPx * nextScale - screenWidthPx) / 2f).coerceAtLeast(0f)
                            val maxPanY = ((targetHeightPx * nextScale - screenHeightPx) / 2f).coerceAtLeast(0f)
                            userOffset = Offset(
                                x = (userOffset.x + pan.x).coerceIn(-maxPanX, maxPanX),
                                y = (userOffset.y + pan.y).coerceIn(-maxPanY, maxPanY)
                            )
                        }
                    }
            )
        }
    }
}

@Composable
private fun rememberCoilImageBitmap(
    imageUrl: String
): State<ImageBitmap?> {
    val context = LocalContext.current
    val imageLoader = remember(context) { SingletonImageLoader.get(context) }
    val memoryCacheKey = remember(imageUrl) { MemoryCache.Key(imageUrl) }
    val cachedImage = remember(imageLoader, memoryCacheKey) {
        imageLoader.memoryCache?.get(memoryCacheKey)?.image?.toImageBitmap()
    }

    return produceState(
        initialValue = cachedImage,
        imageLoader,
        imageUrl,
        memoryCacheKey
    ) {
        if (value != null) return@produceState

        val cached = imageLoader.memoryCache?.get(memoryCacheKey)?.image?.toImageBitmap()
        if (cached != null) {
            value = cached
            return@produceState
        }

        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .memoryCacheKey(memoryCacheKey)
            .build()
        val result = imageLoader.execute(request)
        if (result is SuccessResult) {
            value = result.image.toImageBitmap()
        }
    }
}

private fun CoilImage.toImageBitmap(): ImageBitmap {
    return when (this) {
        is BitmapImage -> bitmap.asImageBitmap()
        else -> toBitmap(width.coerceAtLeast(1), height.coerceAtLeast(1)).asImageBitmap()
    }
}

private fun PhotoItemModel.ratio(): Float {
    return (width.toFloat() / height.toFloat())
        .takeIf { it.isFinite() && it > 0f } ?: 1f
}

private fun Dp.lerpTo(
    target: Dp,
    fraction: Float
): Dp {
    return this + (target - this) * fraction
}

private val OffsetSaver = androidx.compose.runtime.saveable.Saver<Offset, List<Float>>(
    save = { listOf(it.x, it.y) },
    restore = { Offset(it[0], it[1]) }
)

private const val MaxZoomScale = 5f
