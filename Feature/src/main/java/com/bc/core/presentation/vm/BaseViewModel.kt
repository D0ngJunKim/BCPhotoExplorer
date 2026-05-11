package com.bc.core.presentation.vm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<SideEffect : Any, Intent : Any> : ViewModel() {
    abstract fun processIntent(intent: Intent)

    private val _sideEffects = Channel<SideEffect>(Channel.UNLIMITED)
    val sideEffects: Flow<SideEffect>
        get() = _sideEffects.receiveAsFlow()

    protected fun sendSideEffect(sideEffect: SideEffect) {
        _sideEffects.trySend(sideEffect)
    }
}

inline fun <SideEffect : Any> BaseViewModel<SideEffect, *>.observeSideEffects(
    lifecycle: Lifecycle,
    crossinline sideEffectFunction: (SideEffect) -> Unit
): Job {
    return lifecycle.coroutineScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            sideEffects.collect {
                sideEffectFunction(it)
            }
        }
    }
}