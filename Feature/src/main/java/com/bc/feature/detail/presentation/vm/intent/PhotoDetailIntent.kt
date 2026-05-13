package com.bc.feature.detail.presentation.vm.intent

sealed class PhotoDetailIntent {
    data object OnToggleLike : PhotoDetailIntent()
}

sealed class PhotoDetailSideEffect {
    data class Toast(val message: String) : PhotoDetailSideEffect()
}
