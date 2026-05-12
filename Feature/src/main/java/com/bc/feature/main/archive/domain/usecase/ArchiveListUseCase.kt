package com.bc.feature.main.archive.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.bc.core.data.archive.repostiory.ArchiveRepository
import com.bc.core.domain.model.PhotoItemModel
import com.bc.core.presentation.ui.UiItem
import com.bc.feature.main.archive.data.repository.ArchiveListRepository
import com.bc.feature.main.archive.presentation.unit.toArchiveItem
import com.bc.feature.main.photolist.presentation.vm.intent.PhotoListIntent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ArchiveListUseCase @Inject constructor(
    private val archiveListRepository: ArchiveListRepository,
    private val archiveRepository: ArchiveRepository
) {
    fun getPhotoList(): Flow<PagingData<UiItem<PhotoListIntent>>> {
        return archiveListRepository.getCollectionList()
            .map { pagingData ->
                pagingData.map { photo ->
                    photo.toArchiveItem()
                }
            }
    }

    suspend fun onToggleLike(data: PhotoItemModel) {
        archiveRepository.delete(data)
    }
}
