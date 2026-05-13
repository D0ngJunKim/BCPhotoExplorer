package com.bc.core.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.bc.core.presentation.vm.BaseViewModel
import com.ssg.env.ds.composite.LocalExpandLayout
import com.ssg.env.ds.foundation.SpaceToken
import com.ssg.env.ds.foundation.SpaceTokenValues
import com.ssg.env.ds.foundation.spacedBy

private const val COLUMN_ON_TABLET = 4
private const val COLUMN_ON_MOBILE = 2

enum class ListSpan(private val mobileSpanCount: Int, private val tabletSpanCount: Int) {
    FULL_FOR_ALL(COLUMN_ON_MOBILE, COLUMN_ON_TABLET),
    FULL_ON_MOBILE_HALF_ON_TABLET(COLUMN_ON_MOBILE, COLUMN_ON_TABLET / 2),
    SINGLE_FOR_ALL(1, 1);

    companion object {
        fun getGridCells(isTablet: Boolean): GridCells {
            return GridCells.Fixed(if (isTablet) COLUMN_ON_TABLET else COLUMN_ON_MOBILE)
        }

        fun getStaggeredGridCells(isTablet: Boolean): StaggeredGridCells {
            return StaggeredGridCells.Fixed(if (isTablet) COLUMN_ON_TABLET else COLUMN_ON_MOBILE)
        }
    }

    fun getGridSpan(isTablet: Boolean): GridItemSpan {
        return GridItemSpan(if (isTablet) tabletSpanCount else mobileSpanCount)
    }

    fun getStaggerGridSpan(isTablet: Boolean): StaggeredGridItemSpan {
        val count = if (isTablet) tabletSpanCount else mobileSpanCount
        return if (count == 1) StaggeredGridItemSpan.SingleLane else StaggeredGridItemSpan.FullLine
    }
}


@Composable
fun <Intent : Any> GridList(
    state: LazyGridState,
    viewModel: BaseViewModel<*, Intent>,
    items: List<UiItem<Intent>>,
    modifier: Modifier = Modifier,
    config: ListConfig = rememberListConfig(),
    footerItem: UiItem<Intent>? = remember { ListFooterUiItem() },
) {
    val configuration = LocalConfiguration.current
    val isTablet = remember(configuration) {
        (configuration.screenLayout and (Configuration.SCREENLAYOUT_SIZE_MASK)) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    LazyVerticalGrid(
        columns = ListSpan.getGridCells(isTablet),
        state = state,
        modifier = modifier,
        contentPadding = SpaceTokenValues(horizontal = config.edgeSpace),
        horizontalArrangement = Arrangement.spacedBy(config.horizontalDividerSpace),
        verticalArrangement = Arrangement.spacedBy(config.verticalDividerSpace),
        overscrollEffect = null,
    ) {
        for (index in 0 until items.size) {
            val item = items.getOrNull(index) ?: continue

            if (item.isStickable) {
                stickyHeader(
                    key = item.itemKey,
                    contentType = item::class
                ) {
                    LocalExpandLayout(Modifier.fillMaxWidth(), config.edgeSpace) {
                        item.BuildItem(viewModel::processIntent)
                    }
                }
            } else {
                item(
                    key = item.itemKey,
                    contentType = item::class,
                    span = { item.span.getGridSpan(isTablet) }

                ) {
                    item.BuildItem(viewModel::processIntent)
                }
            }
        }

        if (footerItem != null && items.isNotEmpty()) {
            if (footerItem.isStickable) {
                stickyHeader(
                    key = footerItem.itemKey,
                    contentType = footerItem::class
                ) {
                    LocalExpandLayout(Modifier.fillMaxWidth(), config.edgeSpace) {
                        footerItem.BuildItem(viewModel::processIntent)
                    }
                }
            } else {
                item(
                    key = footerItem.itemKey,
                    contentType = footerItem::class,
                    span = { footerItem.span.getGridSpan(isTablet) }

                ) {
                    footerItem.BuildItem(viewModel::processIntent)
                }
            }
        }
    }
}

@Composable
fun <Intent : Any> PagingStaggeredList(
    state: LazyStaggeredGridState,
    viewModel: BaseViewModel<*, Intent>,
    items: LazyPagingItems<out UiItem<Intent>>,
    modifier: Modifier = Modifier,
    config: ListConfig = rememberListConfig(),
    footerItem: UiItem<Intent>? = remember { ListFooterUiItem() },
) {
    val configuration = LocalConfiguration.current
    val isTablet = remember(configuration) {
        (configuration.screenLayout and (Configuration.SCREENLAYOUT_SIZE_MASK)) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    LazyVerticalStaggeredGrid(
        columns = ListSpan.getStaggeredGridCells(isTablet),
        state = state,
        modifier = modifier,
        contentPadding = SpaceTokenValues(horizontal = config.edgeSpace),
        horizontalArrangement = Arrangement.spacedBy(config.horizontalDividerSpace),
        verticalItemSpacing = Arrangement.spacedBy(config.verticalDividerSpace).spacing,
        overscrollEffect = null,
    ) {
        for (index in 0 until items.itemCount) {
            val item = items.peek(index) ?: continue
            item(
                key = item.itemKey,
                contentType = item::class,
                span = item.span.getStaggerGridSpan(isTablet)
            ) {
                items[index]?.BuildItem(viewModel::processIntent)
            }
        }

        if (footerItem != null && items.itemCount > 0) {
            item(
                key = footerItem.itemKey,
                contentType = footerItem::class,
                span = footerItem.span.getStaggerGridSpan(isTablet)
            ) {
                footerItem.BuildItem(viewModel::processIntent)
            }
        }
    }
}

@Composable
fun rememberListConfig(
    edgeSpace: SpaceToken = SpaceToken.SM,
    horizontalDividerSpace: SpaceToken = SpaceToken.XXS,
    verticalDividerSpace: SpaceToken = SpaceToken.XXS,
    isUseLoadMore: Boolean = true
): ListConfig =
    rememberSaveable(saver = ListConfig.Saver) {
        ListConfig(
            edgeSpace,
            horizontalDividerSpace,
            verticalDividerSpace,
            isUseLoadMore
        )
    }

data class ListConfig(
    val edgeSpace: SpaceToken,
    val horizontalDividerSpace: SpaceToken,
    val verticalDividerSpace: SpaceToken,
    val isUseLoadMore: Boolean,
) {
    companion object {
        val Saver: Saver<ListConfig, *> =
            listSaver(
                save = {
                    listOf(
                        it.edgeSpace.toString(),
                        it.horizontalDividerSpace.toString(),
                        it.verticalDividerSpace.toString(),
                        it.isUseLoadMore
                    )
                },
                restore = {
                    ListConfig(
                        edgeSpace = SpaceToken.valueOf(it[0].toString()),
                        horizontalDividerSpace = SpaceToken.valueOf(it[1].toString()),
                        verticalDividerSpace = SpaceToken.valueOf(it[2].toString()),
                        isUseLoadMore = it[3].toString().toBoolean(),
                    )
                },
            )
    }
}

private class ListFooterUiItem<Intent : Any> : UiItem<Intent>() {
    @Composable
    override fun SetItem(processIntent: ((Intent) -> Unit)) {
        Spacer(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .height(100.dp)
        )
    }
}
