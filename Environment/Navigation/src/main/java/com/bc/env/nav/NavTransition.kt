package com.bc.env.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

typealias EnterAnim = AnimatedContentTransitionScope<IRoute?>.() -> EnterTransition
typealias ExitAnim = AnimatedContentTransitionScope<IRoute?>.() -> ExitTransition

private const val ANIM_DURATION = 300

data class NavTransition(
    internal val enter: EnterAnim,
    internal val exit: ExitAnim,
    internal val popEnter: EnterAnim,
    internal val popExit: ExitAnim
) {
    companion object {
        val Immediate = NavTransition(
            enter = { EnterTransition.None },
            exit = { ExitTransition.None },
            popEnter = { EnterTransition.None },
            popExit = { ExitTransition.None }
        )

        val SlideHorizontal = NavTransition(
            enter = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(ANIM_DURATION)
                )
            },
            exit = {
                slideOutHorizontally(
                    targetOffsetX = { -it / 3 },
                    animationSpec = tween(ANIM_DURATION)
                ) + fadeOut(
                    targetAlpha = 0.9f,
                    animationSpec = tween(ANIM_DURATION)
                )
            },
            popEnter = {
                slideInHorizontally(
                    initialOffsetX = { -it / 3 },
                    animationSpec = tween(ANIM_DURATION)
                ) + fadeIn(
                    initialAlpha = 0.9f,
                    animationSpec = tween(ANIM_DURATION)
                )
            },
            popExit = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(ANIM_DURATION)
                )
            }
        )
    }
}