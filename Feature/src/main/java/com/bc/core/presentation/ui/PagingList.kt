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
import java.util.UUID

private const val COLUMN_ON_TABLET = 4
private const val COLUMN_ON_MOBILE = 2

enum class ListSpan(private val mobileSpanCount: Int, private val tabletSpanCount: Int) {
    FULL_FOR_ALL(COLUMN_ON_MOBILE, COLUMN_ON_TABLET),
    FULL_ON_MOBILE_HALF_ON_TABLET(COLUMN_ON_MOBILE, COLUMN_ON_TABLET / 2),
    SINGLE_FOR_ALL(1, 1);

    companion object {
        fun getColumn(isTablet: Boolean): GridCells {
            return GridCells.Fixed(if (isTablet) COLUMN_ON_TABLET else COLUMN_ON_MOBILE)
        }
    }

    fun getSpanCount(isTablet: Boolean): Int {
        return if (isTablet) tabletSpanCount else mobileSpanCount
    }
}


@Composable
fun <Intent : Any> PagingList(
    state: LazyGridState,
    modifier: Modifier = Modifier,
    config: ListConfig = rememberListConfig(),
    viewModel: BaseViewModel<*, Intent>,
    items: LazyPagingItems<UiItem<Intent>>,
    footerItem: UiItem<Intent>? = remember { ListFooterUiItem() },
) {
    val configuration = LocalConfiguration.current
    val isTablet = remember(configuration) {
        (configuration.screenLayout and (Configuration.SCREENLAYOUT_SIZE_MASK)) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    LazyVerticalGrid(
        columns = ListSpan.getColumn(isTablet),
        state = state,
        modifier = modifier,
        contentPadding = SpaceTokenValues(horizontal = config.edgeSpace),
        horizontalArrangement = Arrangement.spacedBy(config.horizontalDividerSpace),
        verticalArrangement = Arrangement.spacedBy(config.verticalDividerSpace),
        overscrollEffect = null,
    ) {
        for (index in 0 until items.itemCount) {
            val item = items.peek(index) ?: continue

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
                    span = { GridItemSpan(item.span.getSpanCount(isTablet)) }

                ) {
                    items[index]?.BuildItem(viewModel::processIntent)
                }
            }
        }

        if (footerItem != null && items.itemCount > 0) {
            if (footerItem.isStickable) {
                stickyHeader(
                    key = footerItem.itemKey,
                    contentType = footerItem::class
                ) {
                    LocalExpandLayout(Modifier.fillMaxWidth(), config.edgeSpace) {
                        footerItem.BuildItem(viewModel::processIntent)
                    }
                }
            }
            item(
                key = footerItem.itemKey,
                contentType = footerItem::class,
                span = { GridItemSpan(footerItem.span.getSpanCount(isTablet)) }

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

abstract class UiItem<Intent : Any>(val span: ListSpan = ListSpan.FULL_FOR_ALL) {
    open val itemKey: String = UUID.randomUUID().toString()
    open val isStickable: Boolean = false

    @Composable
    fun BuildItem(
        processIntent: ((Intent) -> Unit)
    ) {
        SetItem(processIntent)
    }

    @Composable
    protected abstract fun SetItem(
        processIntent: ((Intent) -> Unit)
    )
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
