package com.bc.feature.main.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bc.env.nav.IRoute
import com.bc.env.nav.IRouteConfig
import com.bc.env.nav.NavTransition
import com.bc.env.nav.annotation.MainContainer
import com.ssg.env.ds.composite.LocalText
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        LocalText("asdlkjfalsdk;fjaslk;dfjkl;asdfjlkasd;fjlkasdfadls")
    }
}