package com.bc.feature.main.photolist.presentation.vm

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.bc.core.presentation.ui.UiItem
import com.bc.core.presentation.vm.BaseViewModel
import com.bc.feature.main.photolist.domain.usecase.PhotoListUseCase
import com.bc.feature.main.photolist.presentation.vm.intent.PhotoListIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val useCase: PhotoListUseCase
) : BaseViewModel<Any, PhotoListIntent>() {
    val items: Flow<PagingData<UiItem<PhotoListIntent>>> =
        useCase.getPhotoList(viewModelScope)

    override fun processIntent(intent: PhotoListIntent) {
        when (intent) {
            is PhotoListIntent.OnToggleLike -> {
                viewModelScope.launch(Dispatchers.IO){
                    useCase.onToggleLike(intent.data)
                }
            }
        }
    }
}
