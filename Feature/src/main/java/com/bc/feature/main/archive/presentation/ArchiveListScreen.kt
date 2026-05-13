package com.bc.feature.main.archive.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.bc.core.presentation.ui.PagingStaggeredList
import com.bc.feature.R
import com.bc.feature.main.archive.presentation.vm.ArchiveListViewModel
import com.ssg.env.ds.composite.LocalCircularProgressIndicator
import com.ssg.env.ds.composite.LocalImage
import com.ssg.env.ds.composite.LocalText
import com.ssg.env.ds.foundation.SpaceToken
import com.ssg.env.ds.foundation.padding

@Composable
fun ArchiveListScreen(
    state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    viewModel: ArchiveListViewModel = hiltViewModel(),
) {
    val items = viewModel.items.collectAsLazyPagingItems()
    val isLoading = items.loadState.refresh is LoadState.Loading
    val isEmpty = items.itemCount == 0 && !isLoading

    Box(modifier = Modifier.fillMaxSize()) {
        PagingStaggeredList(
            state = state,
            viewModel = viewModel,
            items = items
        )

        if (isEmpty) {
            ListEmptyScreen()
        }

        if (isLoading) {
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
private fun ListEmptyScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LocalImage(
            painter = painterResource(R.drawable.ico_face_wink),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        LocalText(
            text = "마음에 드는 사진을 탐색해보세요!",
            modifier = Modifier.padding(top = SpaceToken.XXS)
        )
    }
}