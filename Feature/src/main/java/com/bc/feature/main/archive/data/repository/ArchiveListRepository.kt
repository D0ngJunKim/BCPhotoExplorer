package com.bc.feature.main.archive.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bc.core.data.archive.db.ArchiveDao
import com.bc.core.data.archive.db.toDomain
import com.bc.core.domain.model.PhotoItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ArchiveListRepository @Inject constructor(
    private val archiveDao: ArchiveDao,
) {
    fun getCollectionList(): Flow<PagingData<PhotoItemModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 10
            ),
            pagingSourceFactory = { archiveDao.getCollections() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }
}