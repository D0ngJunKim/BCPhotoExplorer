package com.bc.env.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberGlobalNavigator(
    mainNavController: NavHostController = rememberNavController(),
    overlayNavController: NavHostController = rememberNavController(),
): GlobalNavigator {
    return remember(
        mainNavController,
        overlayNavController,
    ) {
        GlobalNavigator(
            mainNavController = mainNavController,
            overlayNavController = overlayNavController,
        )
    }
}

@Stable
class GlobalNavigator(
    internal val mainNavController: NavHostController,
    internal val overlayNavController: NavHostController,
) {
    fun navigate(
        route: IRoute,
        targetContainer: ContainerType? = null,
        navOptions: NavOptions? = null,
        navigatorExtras: Navigator.Extras? = null
    ) {
        val controller = resolveController(route, targetContainer)
        controller.navigate(
            route = route,
            navOptions = navOptions ?: defaultNavOptions(),
            navigatorExtras = navigatorExtras
        )
    }

    fun navigate(
        route: IRoute,
        targetContainer: ContainerType? = null,
        builder: (NavOptionsBuilder.() -> Unit)
    ) {
        val controller = resolveController(route, targetContainer)
        controller.navigate(route) {
            restoreState = true
            builder()
        }
    }

    private fun defaultNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setRestoreState(true)
            .build()
    }

    fun navigateBack(): Boolean {
        if (shouldPop(overlayNavController)) {
            overlayNavController.popBackStack()
            return true
        }

        if (shouldPop(mainNavController)) {
            mainNavController.popBackStack()
            return true
        }

        return false
    }

    fun isEmpty(containerType: ContainerType): Boolean {
        val controller = getController(containerType)
        val currentDestinationId = controller.currentBackStackEntry?.destination?.id ?: return true
        val emptyRouteId = controller.graph.findNode(EmptyRoute::class)?.id ?: return false

        if (currentDestinationId != emptyRouteId) {
            return false
        }

        val previousDestinationId = controller.previousBackStackEntry?.destination?.id
        return previousDestinationId == null || previousDestinationId == emptyRouteId
    }

    private fun getController(targetContainer: ContainerType): NavHostController = when (targetContainer) {
        ContainerType.MAIN -> mainNavController
        ContainerType.OVERLAY -> overlayNavController
    }

    private fun resolveController(
        route: IRoute,
        targetContainer: ContainerType?
    ): NavHostController {
        val searchOrder = if (targetContainer == null) {
            ContainerType.entries
        } else {
            listOf(targetContainer) + ContainerType.entries.filterNot { it == targetContainer }
        }

        return searchOrder
            .asSequence()
            .map(::getController)
            .firstOrNull { controller ->
                hasRouteDestination(controller, route)
            } ?: error("Route destination is not registered: ${route::class.qualifiedName.orEmpty()}")
    }

    private fun hasRouteDestination(
        controller: NavHostController,
        route: IRoute
    ): Boolean {
        return runCatching { controller.graph.findNode(route::class) != null }
            .getOrDefault(false)
    }

    private fun shouldPop(controller: NavHostController): Boolean {
        val currentDestinationId = controller.currentBackStackEntry?.destination?.id ?: return false
        val emptyRouteId = controller.graph.findNode(EmptyRoute::class)?.id
        if (emptyRouteId != null && emptyRouteId == currentDestinationId) {
            return false
        }

        return controller.previousBackStackEntry != null
    }
}

enum class ContainerType {
    MAIN,
    OVERLAY
}
