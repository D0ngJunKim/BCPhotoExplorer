package com.bc.feature.main.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.bc.env.network.datasource.BaseDataSource
import com.bc.env.network.request.LoadParams
import com.bc.env.network.request.Parameters
import com.bc.env.network.response.IResponse
import com.bc.env.network.util.DomainProvider
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.QueryMap

@Composable
fun MainScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

    }
}