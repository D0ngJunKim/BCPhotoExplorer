package com.bc.feature.main.archive.presentation.vm.intent

import com.bc.core.domain.model.PhotoItemModel

sealed class ArchiveListIntent {
    data class OnToggleLike(val data: PhotoItemModel) : ArchiveListIntent()
}