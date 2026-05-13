package com.bc.feature.detail.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bc.core.domain.model.PhotoItemModel
import com.bc.core.presentation.ui.AsyncImageBlurHash
import com.bc.core.presentation.ui.GridList
import com.bc.core.presentation.ui.rememberBlurHashBitmap
import com.bc.core.presentation.ui.rememberListConfig
import com.bc.core.presentation.util.toComposeColorOrNull
import com.bc.core.presentation.vm.observeSideEffects
import com.bc.env.nav.IRoute
import com.bc.env.nav.IRouteConfig
import com.bc.env.nav.LocalGlobalNavigator
import com.bc.env.nav.NavTransition
import com.bc.env.nav.annotation.MainContainer
import com.bc.feature.R
import com.bc.feature.detail.presentation.unit.mapper.withParallaxSpacing
import com.bc.feature.detail.presentation.vm.PhotoDetailUiState
import com.bc.feature.detail.presentation.vm.PhotoDetailViewModel
import com.bc.feature.detail.presentation.vm.intent.PhotoDetailIntent
import com.bc.feature.detail.presentation.vm.intent.PhotoDetailSideEffect
import com.ssg.env.ds.component.IconButton
import com.ssg.env.ds.component.IconButtonColorSet
import com.ssg.env.ds.component.IconButtonConfig
import com.ssg.env.ds.component.IconButtonType
import com.ssg.env.ds.composite.LocalCircularProgressIndicator
import com.ssg.env.ds.composite.LocalImage
import com.ssg.env.ds.foundation.SpaceToken
import com.ssg.env.ds.foundation.padding
import kotlinx.serialization.Serializable
import timber.log.Timber
import kotlin.math.roundToInt

@Serializable
@MainContainer
data class DetailRoute(
    val data: PhotoItemModel
) : IRoute.Screen {

    init {
        Timber.tag("KDJ").d("DetailRoute.init: ${data.blurHash}")
    }

    companion object : IRouteConfig.Screen {
        override val transition: NavTransition = NavTransition.SlideHorizontal
    }

    @Composable
    override fun Content() {
        Timber.tag("KDJ").d("DetailRoute.Content: ${data.blurHash}")

        PhotoDetailScreen(
            data = data
        )
    }
}

@Composable
fun PhotoDetailScreen(
    data: PhotoItemModel,
    gridState: LazyGridState = rememberLazyGridState(),
    viewModel: PhotoDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(data.id) {
        viewModel.loadApi(data)
    }

    DisposableEffect(lifecycleOwner, viewModel) {
        val job = viewModel.observeSideEffects(lifecycleOwner.lifecycle) { sideEffect ->
            when (sideEffect) {
                is PhotoDetailSideEffect.Toast -> {
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        onDispose {
            job.cancel()
        }
    }

    PhotoDetailContent(
        uiState = uiState,
        gridState = gridState,
        viewModel = viewModel
    )
}

@Composable
private fun PhotoDetailContent(
    uiState: PhotoDetailUiState,
    gridState: LazyGridState,
    viewModel: PhotoDetailViewModel
) {
    val photo = uiState.photo ?: return
    val primaryColor = photo.primaryColor.toComposeColorOrNull() ?: Color.Black
    val ratio = photo.ratio()


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val density = LocalDensity.current
        val imgHeight = maxWidth / ratio
        val imageWidthPx = with(density) { maxWidth.roundToPx() }
        val imgHeightPx = with(density) { imgHeight.roundToPx() }
        val parallaxScrollPx by remember(gridState, imgHeightPx) {
            derivedStateOf {
                if (gridState.firstVisibleItemIndex == 0) {
                    gridState.firstVisibleItemScrollOffset
                } else {
                    imgHeightPx
                }
            }
        }
        val parallaxOffsetPx by remember(imgHeightPx) {
            derivedStateOf {
                (parallaxScrollPx * 0.45f)
                    .coerceAtMost(imgHeightPx * 0.45f)
                    .roundToInt()
            }
        }
        val contentBackgroundOffsetPx by remember(imgHeightPx) {
            derivedStateOf {
                (imgHeightPx - parallaxScrollPx)
                    .coerceAtLeast(0)
            }
        }

        AsyncImageBlurHash(
            model = photo.imageUrl,
            blurHash = photo.blurHash,
            contentDescription = photo.altDescription,
            width = imageWidthPx,
            height = imgHeightPx,
            primaryColor = primaryColor.copy(alpha = 0.5f),
            modifier = Modifier
                .offset {
                    IntOffset(x = 0, y = -parallaxOffsetPx)
                }
                .fillMaxWidth()
                .height(with(density) { imgHeightPx.toDp() })
                .aspectRatio(ratio)
        )

        BlurContentBackground(
            photo = photo,
            primaryColor = primaryColor,
            width = imageWidthPx,
            height = imgHeightPx,
            contentOffsetPx = contentBackgroundOffsetPx,
            parallaxOffsetPx = parallaxOffsetPx
        )

        GridList(
            state = gridState,
            viewModel = viewModel,
            items = uiState.dataList.withParallaxSpacing(imgHeight),
            modifier = Modifier.fillMaxSize(),
            config = rememberListConfig(
                edgeSpace = SpaceToken.SM,
                horizontalDividerSpace = SpaceToken.XXS,
                verticalDividerSpace = SpaceToken.SM
            )
        )

        Header(
            isArchived = uiState.isArchived,
            onToggleLike = {
                viewModel.processIntent(PhotoDetailIntent.OnToggleLike)
            }
        )

        if (uiState.isLoading) {
            LocalCircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center),
                color = colorResource(R.color.gray900)
            )
        }
    }
}

@Composable
private fun BlurContentBackground(
    photo: PhotoItemModel,
    primaryColor: Color,
    width: Int,
    height: Int,
    contentOffsetPx: Int,
    parallaxOffsetPx: Int
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(x = 0, y = contentOffsetPx) }
            .clipToBounds()
    ) {
        val density = LocalDensity.current
        val blurBitmap by rememberBlurHashBitmap(
            blurHash = photo.blurHash,
            width = width,
            height = height,
        )

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = 0,
                        y = -contentOffsetPx - parallaxOffsetPx
                    )
                }
                .fillMaxWidth()
                .height(with(density) { height.toDp() })
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.8f))
            )

            val currentBitmap = blurBitmap
            if (currentBitmap != null) {
                LocalImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.5f),
                    bitmap = currentBitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun Header(
    isArchived: Boolean,
    onToggleLike: () -> Unit
) {
    val navigator = LocalGlobalNavigator.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = SpaceToken.SM, vertical = SpaceToken.XS),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            config = IconButtonConfig(
                type = IconButtonType.XL,
                radius = IconButtonConfig.Option.Radius.Oval,
                normalColorSet = IconButtonColorSet(
                    fillColor = Color.White,
                    iconColor = colorResource(R.color.gray900)
                )
            ),
            painter = painterResource(R.drawable.ico_arrow_left),
            onClick = {
                navigator?.navigateBack()
            },
            buttonDescription = "뒤로가기"
        )

        IconButton(
            config = IconButtonConfig(
                type = IconButtonType.XL,
                radius = IconButtonConfig.Option.Radius.Oval,
                normalColorSet = IconButtonColorSet(
                    fillColor = Color.White,
                    iconColor = colorResource(R.color.gray900)
                ),
                selectedColorSet = IconButtonColorSet(
                    fillColor = Color.White,
                    iconColor = colorResource(R.color.primary)
                )
            ),
            painter = painterResource(R.drawable.ico_heart),
            onClick = onToggleLike,
            buttonDescription = "좋아요",
            selected = isArchived
        )
    }
}

private fun PhotoItemModel.ratio(): Float {
    return (width.toFloat() / height.toFloat())
        .takeIf { it.isFinite() && it > 0f } ?: 1f
}
