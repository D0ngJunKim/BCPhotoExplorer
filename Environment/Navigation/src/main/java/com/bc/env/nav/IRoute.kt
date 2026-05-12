package com.bc.env.nav

import android.net.Uri
import android.os.Bundle
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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

    fun typeMap(route: KClass<out IRoute>): Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap()
}

@PublishedApi
internal val escapedPercent = "\\" + "u0025"

@PublishedApi
internal fun String.escapePercentForNavigation(): String {
    return replace("%", escapedPercent)
}

inline fun <reified T : Any> serializableNavType(): NavType<T> {
    return object : NavType<T>(isNullableAllowed = false) {
        override fun put(bundle: Bundle, key: String, value: T) {
            bundle.putString(key, Json.encodeToString(value).escapePercentForNavigation())
        }

        override fun get(bundle: Bundle, key: String): T? {
            return bundle.getString(key)?.let { Json.decodeFromString<T>(it) }
        }

        override fun parseValue(value: String): T {
            return Json.decodeFromString(value)
        }

        override fun serializeAsValue(value: T): String {
            return Uri.encode(Json.encodeToString(value).escapePercentForNavigation())
        }
    }
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
