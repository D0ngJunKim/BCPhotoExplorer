package com.bc.feature.main.archive.domain.usecase

import androidx.paging.PagingData
import com.bc.core.domain.model.PhotoItemModel
import com.bc.core.domain.repository.ArchiveRepository
import com.bc.feature.main.archive.domain.repository.ArchiveListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArchiveListUseCase @Inject constructor(
    private val archiveListRepository: ArchiveListRepository,
    private val archiveRepository: ArchiveRepository
) {
    fun getPhotoList(): Flow<PagingData<PhotoItemModel>> =
        archiveListRepository.getCollectionList()

    suspend fun onToggleLike(data: PhotoItemModel) {
        archiveRepository.delete(data)
    }
}
