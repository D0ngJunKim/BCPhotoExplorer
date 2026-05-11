package com.bc.feature.main.photolist.presentation.vm.intent

import com.bc.core.domain.model.PhotoItemModel

sealed class PhotoListIntent {
    data class OnToggleLike(val data: PhotoItemModel) : PhotoListIntent()
}