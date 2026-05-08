package com.bc.feature.detail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bc.env.nav.IRoute
import com.bc.env.nav.IRouteConfig
import com.bc.env.nav.NavTransition
import com.bc.env.nav.annotation.MainContainer
import kotlinx.serialization.Serializable

@Serializable
@MainContainer
data class DetailRoute(
    val photoId: String
) : IRoute.Screen {
    companion object : IRouteConfig.Screen {
        override val transition: NavTransition = NavTransition.SlideHorizontal
    }

    @Composable
    override fun Content() {
        DetailScreen(photoId = photoId)
    }
}

@Composable
fun DetailScreen(
    photoId: String,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // TODO: photoId를 사용하는 상세 화면을 구성합니다.
    }
}
