package com.bc.feature.main.archive.presentation.vm

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.bc.core.presentation.vm.BaseViewModel
import com.bc.feature.main.archive.domain.usecase.ArchiveListUseCase
import com.bc.feature.main.archive.presentation.unit.mapper.toArchiveItem
import com.bc.feature.main.photolist.presentation.vm.intent.PhotoListIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveViewModel @Inject constructor(
    private val useCase: ArchiveListUseCase
) : BaseViewModel<Any, PhotoListIntent>() {
    val items = useCase.getPhotoList()
        .map { pagingData ->
            pagingData.map { photo ->
                photo.toArchiveItem()
            }
        }
        .cachedIn(viewModelScope)

    override fun processIntent(intent: PhotoListIntent) {
        when (intent) {
            is PhotoListIntent.OnToggleLike -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCase.onToggleLike(intent.data)
                }
            }
        }
    }
}
