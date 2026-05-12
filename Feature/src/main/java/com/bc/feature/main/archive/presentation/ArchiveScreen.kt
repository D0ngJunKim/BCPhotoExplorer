package com.bc.feature.main.archive.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.bc.core.presentation.ui.PagingList
import com.bc.feature.R
import com.bc.feature.main.archive.presentation.vm.ArchiveViewModel
import com.ssg.env.ds.composite.LocalCircularProgressIndicator

@Composable
fun ArchiveScreen(
    state: LazyGridState = rememberLazyGridState(),
    viewModel: ArchiveViewModel = hiltViewModel(),
) {
    val items = viewModel.items.collectAsLazyPagingItems()

    Box(modifier = Modifier.fillMaxSize()) {
        PagingList(
            state = state,
            viewModel = viewModel,
            items = items
        )

        if (items.loadState.refresh is LoadState.Loading) {
            LocalCircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center),
                color = colorResource(R.color.gray900)
            )
        }
    }
}
