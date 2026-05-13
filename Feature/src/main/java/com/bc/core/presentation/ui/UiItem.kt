package com.bc.core.presentation.ui

import androidx.compose.runtime.Composable
import java.util.UUID

abstract class UiItem<Intent : Any>(val span: ListSpan = ListSpan.FULL_FOR_ALL) {
    open val itemKey: String = UUID.randomUUID().toString()
    open val isStickable: Boolean = false

    @Composable
    fun BuildItem(
        processIntent: ((Intent) -> Unit)
    ) {
        SetItem(processIntent)
    }

    @Composable
    protected abstract fun SetItem(
        processIntent: ((Intent) -> Unit)
    )
}
