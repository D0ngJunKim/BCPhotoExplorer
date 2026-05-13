package com.bc.feature.main.archive.domain.repository

import androidx.paging.PagingData
import com.bc.core.domain.model.PhotoItemModel
import kotlinx.coroutines.flow.Flow

interface ArchiveListRepository {
    fun getCollectionList(): Flow<PagingData<PhotoItemModel>>
}
