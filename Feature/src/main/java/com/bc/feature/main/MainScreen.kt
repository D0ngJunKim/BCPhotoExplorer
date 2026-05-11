package com.bc.feature.main

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.bc.env.nav.IRoute
import com.bc.env.nav.IRouteConfig
import com.bc.env.nav.NavTransition
import com.bc.env.nav.annotation.MainContainer
import com.bc.feature.main.photolist.presentation.PhotoListScreen
import com.ssg.env.ds.R
import com.ssg.env.ds.composite.LocalImage
import com.ssg.env.ds.composite.LocalText
import com.ssg.env.ds.foundation.RadiusToken
import com.ssg.env.ds.foundation.SpaceToken
import com.ssg.env.ds.foundation.SpaceTokenValues
import com.ssg.env.ds.foundation.background
import com.ssg.env.ds.foundation.padding
import com.ssg.env.ds.foundation.shadow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
@MainContainer(start = true)
class MainRoute : IRoute.Screen {
    companion object : IRouteConfig.Screen {
        override val transition: NavTransition = NavTransition.Immediate
    }

    @Composable
    override fun Content() {
        MainScreen()
    }
}

@Composable
fun MainScreen() {
    val isInspectMode = LocalInspectionMode.current
    val tabs = listOf(
        TabInfo(R.drawable.ico_navigation, "탐색", rememberLazyGridState()),
        TabInfo(R.drawable.ico_heart, "좋아요", rememberLazyGridState())
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )

    var isCollapsed by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        LaunchedEffect(pagerState.currentPage) {
            val currentListState = tabs[pagerState.currentPage].listState
            snapshotFlow { currentListState.firstVisibleItemIndex }
                .distinctUntilChanged()
                .collect { firstIndex ->
                    isCollapsed = firstIndex > 0
                }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            HorizontalPager(
                userScrollEnabled = false,
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize(),
                beyondViewportPageCount = 1,
                key = { page -> tabs[page].tabNm }
            ) { page ->
                if (isInspectMode.not()) {
                    when (page) {
                        0 -> PhotoListScreen(tabs[page].listState)
                        1 -> PhotoListScreen(tabs[page].listState)
                    }
                }
            }

            TabBar(
                tabs = tabs,
                pagerState = pagerState,
                isCollapsed = isCollapsed,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun TabBar(
    tabs: List<TabInfo>,
    pagerState: PagerState,
    isCollapsed: Boolean,
    modifier: Modifier,
) {
    val scope = rememberCoroutineScope()

    val paddingValues = SpaceTokenValues(
        start = SpaceToken.SM,
        end = SpaceToken.SM,
        bottom = SpaceToken.XXL
    )
    val screenWidth = with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }
    val fullTabBarWidth = screenWidth - (paddingValues.calculateLeftPadding(LayoutDirection.Ltr) +
            paddingValues.calculateRightPadding(LayoutDirection.Ltr))

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isCollapsed) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "alphaAnimation"
    )
    val animatedShadow by animateFloatAsState(
        targetValue = if (isCollapsed) 4f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "shadowAnimation"
    )
    val animatedTabBarWidth by animateDpAsState(
        targetValue = if (isCollapsed) 100.dp else fullTabBarWidth,
        animationSpec = tween(durationMillis = 300),
        label = "tabBarWidthAnimation"
    )

    Column(
        modifier = modifier
            .padding(paddingValues)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.BottomEnd)
                    .shadow(
                        elevation = Dp(animatedShadow),
                        token = RadiusToken.XL
                    )
                    .background(
                        color = Color.White.compositeOver(Color.White.copy(animatedAlpha)),
                        token = RadiusToken.XL
                    )
                    .clickable {
                        tabs[pagerState.currentPage].listState.requestScrollToItem(0)
                    }) {
                LocalImage(
                    painter = painterResource(R.drawable.ico_arrow_small_up),
                    contentDescription = "위로가기",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(animatedAlpha)
                )
            }

            Row(
                modifier = Modifier
                    .background(color = Color.White, token = RadiusToken.Circle)
                    .align(Alignment.BottomCenter)
                    .width(animatedTabBarWidth)
                    .height(40.dp)
            ) {
                for ((index, tab) in tabs.withIndex()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                                scope.launch {
                                    tab.listState.stopScroll()
                                    pagerState.scrollToPage(tabs.indexOf(tab))
                                }
                            }
                    ) {
                        val color = if (pagerState.currentPage == index) Color.Red else Color.DarkGray
                        if (isCollapsed) {
                            Image(
                                painter = painterResource(tab.iconResId),
                                contentDescription = tab.tabNm,
                                colorFilter = ColorFilter.tint(color),
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center)
                            )

                        } else {
                            LocalText(
                                text = tab.tabNm,
                                color = color,
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .wrapContentHeight()
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class TabInfo(
    @DrawableRes val iconResId: Int,
    val tabNm: String,
    val listState: LazyGridState
)
