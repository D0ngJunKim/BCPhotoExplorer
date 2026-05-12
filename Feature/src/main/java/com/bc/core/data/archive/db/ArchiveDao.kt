package com.bc.core.data.archive.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArchiveDao {
    @Query("SELECT id FROM collection")
    fun getCollectionIds(): Flow<List<String>>

    @Query("SELECT * FROM collection")
    fun getCollections(): PagingSource<Int, CollectionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(collection: CollectionEntity)

    @Delete
    suspend fun delete(collection: CollectionEntity)

    @Query("SELECT * FROM collection WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): CollectionEntity?
}
