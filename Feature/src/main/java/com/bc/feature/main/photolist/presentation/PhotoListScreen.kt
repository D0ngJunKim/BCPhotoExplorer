package com.bc.feature.main.photolist.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.bc.core.presentation.ui.PagingStaggeredList
import com.bc.core.presentation.vm.observeSideEffects
import com.bc.feature.R
import com.bc.feature.main.photolist.presentation.vm.PhotoListViewModel
import com.bc.feature.main.photolist.presentation.vm.intent.PhotoListSideEffect
import com.ssg.env.ds.composite.LocalCircularProgressIndicator
import com.ssg.env.ds.composite.LocalImage
import com.ssg.env.ds.composite.LocalText
import com.ssg.env.ds.foundation.SpaceToken
import com.ssg.env.ds.foundation.padding

@Composable
fun PhotoListScreen(
    state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    viewModel: PhotoListViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val items = viewModel.items.collectAsLazyPagingItems()
    val isNetworkConnected by viewModel.isNetworkConnected.collectAsStateWithLifecycle()
    val hasProblem = items.loadState.source.isIdle || items.loadState.source.hasError

    DisposableEffect(lifecycleOwner, viewModel) {
        val job = viewModel.observeSideEffects(lifecycleOwner.lifecycle) { sideEffect ->
            when (sideEffect) {
                is PhotoListSideEffect.Toast -> {
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        onDispose {
            job.cancel()
        }
    }

    LaunchedEffect(isNetworkConnected) {
        if (isNetworkConnected && hasProblem) {
            items.retry()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PagingStaggeredList(
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

        if (items.itemCount == 0 && (!isNetworkConnected || hasProblem)) {
            NetworkDisconnected()
        }
    }
}

@Composable
private fun NetworkDisconnected() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LocalImage(
            painter = painterResource(R.drawable.ico_wifi_off),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )

        LocalText(
            text = "네트워크가 원활하지 않습니다",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(top = SpaceToken.XS)
        )

        LocalText(
            text = "인터넷 연결을 확인해주세요",
            fontSize = 14.sp,
            modifier = Modifier
                .padding(top = SpaceToken.XXXS)
        )
    }
}