package com.bc.env.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

sealed interface IRoute {
    interface Screen : IRoute {
        @Composable
        fun Content()
    }

    interface Dialog : IRoute {
        @Composable
        fun Content()
    }
}

sealed interface IRouteConfig {
    interface Screen : IRouteConfig {
        val transition: NavTransition get() = NavTransition.SlideHorizontal
    }

    interface Dialog : IRouteConfig {
        val dialogProperties: DialogProperties get() = DialogProperties()
    }
}