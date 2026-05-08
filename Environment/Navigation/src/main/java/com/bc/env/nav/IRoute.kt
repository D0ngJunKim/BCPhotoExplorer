package com.bc.env.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.KType

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
    val typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> get() = emptyMap()
    val deepLinks: List<NavDeepLink> get() = emptyList()

    interface Screen : IRouteConfig {
        val transition: NavTransition get() = NavTransition.SlideHorizontal
    }

    interface Dialog : IRouteConfig {
        val dialogProperties: DialogProperties get() = DialogProperties()
    }
}

interface GeneratedRouteRegistry {
    val routes: List<KClass<out IRoute>>
    val startRoute: KClass<out IRoute>
}

@Serializable
class EmptyRoute : IRoute.Screen {
    companion object : IRouteConfig.Screen {
        override val transition: NavTransition = NavTransition.Immediate
    }

    @Composable
    override fun Content() {
        Spacer(
            Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        )
    }
}
