package com.bc.feature.main.photolist.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.bc.core.presentation.ui.PagingList
import com.bc.core.presentation.vm.observeSideEffects
import com.bc.feature.main.photolist.presentation.vm.PhotoListViewModel
import com.ssg.env.ds.composite.LocalCircularProgressIndicator

@Composable
fun PhotoListScreen(
    state: LazyGridState = rememberLazyGridState(),
    viewModel: PhotoListViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val items = viewModel.items.collectAsLazyPagingItems()

    DisposableEffect(lifecycleOwner, viewModel) {
        val job = viewModel.observeSideEffects(lifecycleOwner.lifecycle) { sideEffect ->


        }

        onDispose {
            job.cancel()
        }
    }

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
                color = colorResource(com.ssg.env.ds.R.color.gray900)
            )
        }
    }
}