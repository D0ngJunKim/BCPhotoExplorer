package com.bc.feature.detail.data.repository

import com.bc.core.domain.model.PhotoItemModel
import com.bc.env.network.request.LoadParams
import com.bc.feature.detail.data.source.PhotoDetailDataSource
import com.bc.core.data.model.toDomain
import javax.inject.Inject

class PhotoDetailRepository @Inject constructor(
    private val dataSource: PhotoDetailDataSource
) {
    suspend fun loadApi(id: String): Result<PhotoItemModel> {
        return dataSource.load(
            LoadParams().put(PhotoDetailDataSource.KEY_ID, id)
        ).mapCatching { dto ->
            dto.toDomain() ?: error("Invalid photo detail response: $id")
        }
    }
}
