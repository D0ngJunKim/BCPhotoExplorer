package com.bc.feature.main

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bc.env.nav.IRoute
import com.bc.env.nav.IRouteConfig
import com.bc.env.nav.NavTransition
import com.bc.env.nav.annotation.MainContainer
import com.bc.feature.R
import com.bc.feature.main.archive.presentation.ArchiveListScreen
import com.bc.feature.main.photolist.presentation.PhotoListScreen
import com.ssg.env.ds.component.IconButton
import com.ssg.env.ds.component.IconButtonColorSet
import com.ssg.env.ds.component.IconButtonConfig
import com.ssg.env.ds.component.IconButtonType
import com.ssg.env.ds.composite.LocalImage
import com.ssg.env.ds.composite.LocalText
import com.ssg.env.ds.foundation.RadiusToken
import com.ssg.env.ds.foundation.ShadowToken
import com.ssg.env.ds.foundation.SpaceToken
import com.ssg.env.ds.foundation.background
import com.ssg.env.ds.foundation.clip
import com.ssg.env.ds.foundation.padding
import com.ssg.env.ds.foundation.shadow
import com.ssg.env.ds.util.asSp
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
        TabInfo(R.drawable.ico_compass, "탐색", rememberLazyStaggeredGridState()),
        TabInfo(R.drawable.ico_heart, "좋아요", rememberLazyStaggeredGridState())
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
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
                    1 -> ArchiveListScreen(tabs[page].listState)
                }
            }
        }

        BottomNavBar(
            tabs = tabs,
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun BottomNavBar(
    tabs: List<TabInfo>,
    pagerState: PagerState,
    modifier: Modifier,
) {
    var isCollapsed by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(pagerState.currentPage) {
        val currentListState = tabs[pagerState.currentPage].listState
        snapshotFlow { currentListState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { firstIndex ->
                isCollapsed = firstIndex > 0
            }
    }

    Row(
        modifier = modifier
            .padding(
                start = SpaceToken.SM,
                end = SpaceToken.SM,
                bottom = SpaceToken.XXL
            )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            ScrollToTopButton(
                visible = isCollapsed,
                onClick = {
                    tabs[pagerState.currentPage].listState.requestScrollToItem(0)
                },
                modifier = Modifier.align(Alignment.BottomEnd)
            )

            TabBar(
                tabs = tabs,
                pagerState = pagerState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun ScrollToTopButton(
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 220)
        ) + fadeIn(animationSpec = tween(durationMillis = 220)),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 180)
        ) + fadeOut(animationSpec = tween(durationMillis = 180)),
        modifier = modifier
    ) {
        IconButton(
            config = IconButtonConfig(
                type = IconButtonType.XL,
                radius = IconButtonConfig.Option.Radius.Oval,
                shadowToken = ShadowToken.MD,
                normalColorSet = IconButtonColorSet(
                    fillColor = Color.White,
                    iconColor = colorResource(R.color.gray900)
                )
            ),
            painter = painterResource(R.drawable.ico_arrow_up),
            buttonDescription = "최상단으로 이동",
            onClick = onClick
        )
    }
}

@Composable
private fun TabBar(
    tabs: List<TabInfo>,
    pagerState: PagerState,
    modifier: Modifier
) {
    val scope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .shadow(ShadowToken.MD, RadiusToken.Circle)
            .background(color = Color.White, token = RadiusToken.Circle)
            .height(40.dp)
            .padding(horizontal = SpaceToken.XXS)
            .clip(RadiusToken.Circle)
    ) {
        for ((index, tab) in tabs.withIndex()) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clickable(
                        interactionSource = null,
                        indication = null
                    ) {
                        scope.launch {
                            tab.listState.stopScroll()
                            pagerState.scrollToPage(tabs.indexOf(tab))
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val color = if (pagerState.currentPage == index) Color.Red else Color.DarkGray

                LocalImage(
                    painter = painterResource(tab.iconResId),
                    contentDescription = tab.tabNm,
                    colorFilter = ColorFilter.tint(color),
                    modifier = Modifier
                        .size(20.dp)
                )

                LocalText(
                    text = tab.tabNm,
                    color = color,
                    fontSize = 8.dp.asSp(),
                    fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(top = SpaceToken.XXXS)
                )
            }
        }
    }
}

private data class TabInfo(
    @DrawableRes val iconResId: Int,
    val tabNm: String,
    val listState: LazyStaggeredGridState
)

@Composable
@Preview
private fun Preview() {
    MainScreen()
}