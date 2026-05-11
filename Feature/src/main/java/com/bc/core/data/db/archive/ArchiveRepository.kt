package com.bc.core.data.db.archive

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArchiveRepository @Inject constructor(
    private val archiveDao: ArchiveDao
) {
    val collectionIdSet: Flow<Set<String>> = archiveDao.getCollectionIds()
        .map { it.toSet() }
        .distinctUntilChanged()

    suspend fun insert(collection: CollectionEntity) {
        archiveDao.insert(collection)
    }

    suspend fun delete(collection: CollectionEntity) {
        archiveDao.delete(collection)
    }
}