package com.bc.feature.main.photolist.domain.usecase

import androidx.paging.PagingData
import com.bc.core.domain.model.PhotoItemModel
import com.bc.core.domain.repository.ArchiveRepository
import com.bc.feature.main.photolist.domain.repository.PhotoListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PhotoListUseCase @Inject constructor(
    private val photoListRepository: PhotoListRepository,
    private val archiveRepository: ArchiveRepository
) {
    val collectionIdSet: Flow<Set<String>> = archiveRepository.collectionIdSet

    fun getPhotoList(): Flow<PagingData<PhotoItemModel>> =
        photoListRepository.getPhotoList()

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
