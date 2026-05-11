package com.bc.feature.main.photolist.domain.usecase

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.bc.core.data.archive.repostiory.ArchiveRepository
import com.bc.core.domain.model.PhotoItemModel
import com.bc.core.presentation.ui.UiItem
import com.bc.feature.main.photolist.data.repository.PhotoListRepository
import com.bc.feature.main.photolist.presentation.unit.toUiItem
import com.bc.feature.main.photolist.presentation.vm.intent.PhotoListIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PhotoListUseCase @Inject constructor(
    private val photoListRepository: PhotoListRepository,
    private val archiveRepository: ArchiveRepository
) {
    fun getPhotoList(scope: CoroutineScope): Flow<PagingData<UiItem<PhotoListIntent>>> =
        combine(
            photoListRepository.getPhotoList().cachedIn(scope),
            archiveRepository.collectionIdSet
        ) { photoList, collectionIdSet ->
            photoList.map { photo ->
                photo.toUiItem(isArchived = collectionIdSet.contains(photo.id))
            }
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
