package com.bc.feature.main.photolist.presentation.vm

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.bc.core.presentation.ui.UiItem
import com.bc.core.presentation.vm.BaseViewModel
import com.bc.env.network.util.NetworkMonitor
import com.bc.feature.main.photolist.domain.usecase.PhotoListUseCase
import com.bc.feature.main.photolist.presentation.vm.intent.PhotoListIntent
import com.bc.feature.main.photolist.presentation.vm.intent.PhotoListSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val useCase: PhotoListUseCase,
    private val networkMonitor: NetworkMonitor
) : BaseViewModel<PhotoListSideEffect, PhotoListIntent>() {
    val items: Flow<PagingData<UiItem<PhotoListIntent>>> =
        useCase.getPhotoList(viewModelScope)

    val isNetworkConnected: StateFlow<Boolean> = networkMonitor.isConnected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    override fun processIntent(intent: PhotoListIntent) {
        when (intent) {
            is PhotoListIntent.OnToggleLike -> {
                viewModelScope.launch {
                    if (networkMonitor.isConnected.first().not()) {
                        sendSideEffect(PhotoListSideEffect.Toast("네트워크가 연결되어 있지 않습니다.\n잠시 후 다시 시도해주세요."))
                    } else {
                        withContext(Dispatchers.IO) {
                            useCase.onToggleLike(intent.data)
                        }
                    }
                }
            }
        }
    }
}
