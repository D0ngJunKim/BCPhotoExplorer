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
import androidx.compose.ui.graphics.Color
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
import com.bc.core.presentation.ui.LikeButton
import com.bc.core.presentation.ui.rememberListConfig
import com.bc.core.presentation.util.toComposeColorOrNull
import com.bc.core.presentation.vm.observeSideEffects
import com.bc.env.nav.IRoute
import com.bc.env.nav.IRouteConfig
import com.bc.env.nav.LocalGlobalNavigator
import com.bc.env.nav.NavTransition
import com.bc.env.nav.annotation.MainContainer
import com.bc.feature.R
import com.bc.feature.detail.presentation.unit.mapper.getDataListWithBackdropSpacing
import com.bc.feature.detail.presentation.vm.PhotoDetailUiState
import com.bc.feature.detail.presentation.vm.PhotoDetailViewModel
import com.bc.feature.detail.presentation.vm.intent.PhotoDetailIntent
import com.bc.feature.detail.presentation.vm.intent.PhotoDetailSideEffect
import com.ssg.env.ds.component.IconButton
import com.ssg.env.ds.component.IconButtonColorSet
import com.ssg.env.ds.component.IconButtonConfig
import com.ssg.env.ds.component.IconButtonType
import com.ssg.env.ds.composite.LocalCircularProgressIndicator
import com.ssg.env.ds.foundation.RadiusToken
import com.ssg.env.ds.foundation.SpaceToken
import com.ssg.env.ds.foundation.background
import com.ssg.env.ds.foundation.padding
import kotlinx.serialization.Serializable
import timber.log.Timber

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
            .background(colorResource(R.color.white))
    ) {
        val density = LocalDensity.current
        val imgHeight = maxWidth / ratio
        val imageWidthPx = with(density) { maxWidth.roundToPx() }
        val imgHeightPx = with(density) { imgHeight.roundToPx() }
        val contentScrollPx by remember(gridState, imgHeightPx) {
            derivedStateOf {
                if (gridState.firstVisibleItemIndex == 0) {
                    gridState.firstVisibleItemScrollOffset
                } else {
                    imgHeightPx
                }
            }
        }
        val backdropOverlapPx = with(density) { 24.dp.roundToPx() }
        val contentBackgroundOffsetPx by remember(imgHeightPx, backdropOverlapPx) {
            derivedStateOf {
                imgHeightPx - contentScrollPx - backdropOverlapPx
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
                .fillMaxWidth()
                .height(with(density) { imgHeightPx.toDp() })
                .aspectRatio(ratio)
        )

        BackdropContentBackground(contentOffsetPx = contentBackgroundOffsetPx)

        GridList(
            state = gridState,
            viewModel = viewModel,
            items = uiState.getDataListWithBackdropSpacing(imgHeight),
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
private fun BackdropContentBackground(contentOffsetPx: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(x = 0, y = contentOffsetPx) }
            .background(colorResource(R.color.white), RadiusToken.XL),
    )
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

        LikeButton(
            onClick = onToggleLike,
            selected = isArchived,
            type = IconButtonType.XL
        )
    }
}

private fun PhotoItemModel.ratio(): Float {
    return (width.toFloat() / height.toFloat())
        .takeIf { it.isFinite() && it > 0f } ?: 1f
}
