package com.bc.feature.detail.domain.usecase

import com.bc.core.domain.model.PhotoItemModel
import com.bc.core.domain.repository.ArchiveRepository
import com.bc.feature.detail.domain.repository.PhotoDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PhotoDetailUseCase @Inject constructor(
    private val photoDetailRepository: PhotoDetailRepository,
    private val archiveRepository: ArchiveRepository
) {
    val collectionIdSet: Flow<Set<String>> = archiveRepository.collectionIdSet

    suspend fun loadApi(id: String): Result<PhotoItemModel> {
        return photoDetailRepository.loadApi(id)
    }

    suspend fun onToggleLike(data: PhotoItemModel) {
        val collectionIdSet = archiveRepository.collectionIdSet.first()
        if (collectionIdSet.contains(data.id)) {
            archiveRepository.delete(data)
        } else {
            archiveRepository.trackDownload(data.trackDownloadUrl)
            archiveRepository.insert(data)
        }
    }
}
