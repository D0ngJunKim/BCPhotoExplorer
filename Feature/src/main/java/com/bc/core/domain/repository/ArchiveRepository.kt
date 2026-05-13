package com.bc.core.domain.repository

import com.bc.core.domain.model.PhotoItemModel
import kotlinx.coroutines.flow.Flow

interface ArchiveRepository {
    val collectionIdSet: Flow<Set<String>>

    suspend fun insert(photo: PhotoItemModel)
    suspend fun delete(photo: PhotoItemModel)
    suspend fun trackDownload(trackDownloadUrl: String?)
}
