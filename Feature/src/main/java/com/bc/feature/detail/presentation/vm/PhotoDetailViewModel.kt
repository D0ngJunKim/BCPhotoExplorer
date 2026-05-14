package com.bc.feature.detail.presentation.vm

import androidx.lifecycle.viewModelScope
import com.bc.core.domain.model.PhotoItemModel
import com.bc.core.presentation.ui.UiItem
import com.bc.core.presentation.vm.BaseViewModel
import com.bc.env.network.util.NetworkMonitor
import com.bc.feature.detail.domain.usecase.PhotoDetailUseCase
import com.bc.feature.detail.presentation.unit.mapper.toPhotoDetailList
import com.bc.feature.detail.presentation.vm.intent.PhotoDetailIntent
import com.bc.feature.detail.presentation.vm.intent.PhotoDetailSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class PhotoDetailUiState(
    val photo: PhotoItemModel? = null,
    val dataList: List<UiItem<PhotoDetailIntent>> = arrayListOf(),
    val isLoading: Boolean = false,
    val isArchived: Boolean = false
)

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val useCase: PhotoDetailUseCase,
    private val networkMonitor: NetworkMonitor
) : BaseViewModel<PhotoDetailSideEffect, PhotoDetailIntent>() {
    private val _uiState = MutableStateFlow(PhotoDetailUiState())
    val uiState: StateFlow<PhotoDetailUiState> = _uiState.asStateFlow()
    private var archiveStateJob: Job? = null

    override fun processIntent(intent: PhotoDetailIntent) {
        when (intent) {
            PhotoDetailIntent.OnToggleLike -> {
                val photo = _uiState.value.photo ?: return
                viewModelScope.launch {
                    if (networkMonitor.isConnected.first().not()) {
                        sendSideEffect(PhotoDetailSideEffect.Toast("네트워크가 연결되어 있지 않습니다.\n잠시 후 다시 시도해주세요."))
                        return@launch
                    }
                    withContext(Dispatchers.IO) {
                        useCase.onToggleLike(photo)
                    }
                }
            }
        }
    }

    fun loadApi(data: PhotoItemModel) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { state ->
                state.copy(
                    photo = data
                )
            }
            observeArchiveState(data.id)

            if (networkMonitor.isConnected.first().not()) {
                _uiState.update { it.copy(isLoading = false) }
                sendSideEffect(PhotoDetailSideEffect.Toast("네트워크가 연결되어 있지 않습니다.\n잠시 후 다시 시도해주세요."))
                return@launch
            }

            val result = withContext(Dispatchers.IO) {
                useCase.loadApi(data.id)
            }
            result
                .onSuccess { result ->
                    _uiState.update { state ->
                        state.copy(
                            photo = result,
                            dataList = result.toPhotoDetailList(),
                            isLoading = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun observeArchiveState(photoId: String) {
        archiveStateJob?.cancel()
        archiveStateJob = viewModelScope.launch {
            useCase.collectionIdSet
                .map { collectionIdSet -> collectionIdSet.contains(photoId) }
                .distinctUntilChanged()
                .collect { isArchived ->
                    _uiState.update { state ->
                        state.copy(isArchived = isArchived)
                    }
                }
        }
    }
}
