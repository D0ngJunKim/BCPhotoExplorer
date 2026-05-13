package com.bc.env.nav

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.isSubclassOf

val LocalGlobalNavigator: ProvidableCompositionLocal<GlobalNavigator?> = compositionLocalOf { null }

@Composable
fun Scaffold(
    mainRoutes: GeneratedRouteRegistry,
    overlayRoutes: GeneratedRouteRegistry,
    modifier: Modifier = Modifier,
    onBack: (GlobalNavigator) -> Unit = { it.navigateBack() },
    navigator: GlobalNavigator = rememberGlobalNavigator(),
) {
    BackHandler {
        onBack(navigator)
    }

    val overlayBackStackEntry by navigator.overlayNavController.currentBackStackEntryAsState()

    val isOverlayTouchBlockEnabled = overlayBackStackEntry != null && !navigator.isEmpty(ContainerType.OVERLAY)

    CompositionLocalProvider(LocalGlobalNavigator provides navigator) {
        Box(modifier = modifier) {
            NavHost(
                navController = navigator.mainNavController,
                startDestination = mainRoutes.startRoute,
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            ) {
                registerRoutes(mainRoutes)
            }

            NavHost(
                navController = navigator.overlayNavController,
                startDestination = overlayRoutes.startRoute,
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .then(
                        if (isOverlayTouchBlockEnabled) {
                            Modifier.clickable(
                                enabled = true,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { }
                        } else {
                            Modifier
                        }
                    )
            ) {
                registerRoutes(overlayRoutes)
            }
        }
    }
}

private fun NavGraphBuilder.registerRoutes(routeRegistry: GeneratedRouteRegistry) {
    routeRegistry.routes.forEach { route ->
        if (route.isSubclassOf(IRoute.Screen::class)) {
            registerScreenRoute(routeRegistry, route)
        } else if (route.isSubclassOf(IRoute.Dialog::class)) {
            registerDialogRoute(routeRegistry, route)
        }
    }
}

private fun NavGraphBuilder.registerScreenRoute(
    routeRegistry: GeneratedRouteRegistry,
    routeClass: KClass<out IRoute>
) {
    val config = routeClass.resolveScreenConfig()
    val transition = config.transition
    val typeMap = routeRegistry.typeMap(routeClass)

    composable(
        route = routeClass,
        typeMap = typeMap,
        deepLinks = config.deepLinks,
        enterTransition = transition.enter,
        exitTransition = transition.exit,
        popEnterTransition = transition.popEnter,
        popExitTransition = transition.popExit
    ) { backStackEntry ->
        backStackEntry.toRoute<IRoute.Screen>(routeClass).Content()
    }
}

private fun NavGraphBuilder.registerDialogRoute(
    routeRegistry: GeneratedRouteRegistry,
    routeClass: KClass<out IRoute>
) {
    val config = routeClass.resolveDialogConfig()
    val dialogProperties = config.dialogProperties
    val typeMap = routeRegistry.typeMap(routeClass)

    dialog(
        route = routeClass,
        typeMap = typeMap,
        deepLinks = config.deepLinks,
        dialogProperties = dialogProperties
    ) { backStackEntry ->
        backStackEntry.toRoute<IRoute.Dialog>(routeClass).Content()
    }
}

private fun KClass<out IRoute>.resolveScreenConfig(): IRouteConfig.Screen {
    return requireNotNull(runCatching { companionObjectInstance as? IRouteConfig.Screen }.getOrNull()) {
        "IRoute.Screen ${qualifiedName.orEmpty()} 는 companion object : IRoute.ScreenConfig를 선언해야 합니다."
    }
}

private fun KClass<out IRoute>.resolveDialogConfig(): IRouteConfig.Dialog {
    return requireNotNull(runCatching { companionObjectInstance as? IRouteConfig.Dialog }.getOrNull()) {
        "IRoute.Dialog ${qualifiedName.orEmpty()} 는 companion object : IRoute.DialogConfig를 선언해야 합니다."
    }
}
