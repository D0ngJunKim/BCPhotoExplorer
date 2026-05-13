package com.bc.feature.detail.data.repository

import com.bc.core.data.model.toDomain
import com.bc.core.domain.model.PhotoItemModel
import com.bc.env.network.request.LoadParams
import com.bc.feature.detail.data.source.PhotoDetailDataSource
import com.bc.feature.detail.domain.repository.PhotoDetailRepository
import javax.inject.Inject

class PhotoDetailRepositoryImpl @Inject constructor(
    private val dataSource: PhotoDetailDataSource
) : PhotoDetailRepository {
    override suspend fun loadApi(id: String): Result<PhotoItemModel> {
        return dataSource.load(
            LoadParams().put(PhotoDetailDataSource.KEY_ID, id)
        ).mapCatching { dto ->
            dto.toDomain() ?: error("누락된 데이터가 존재합니다.")
        }
    }
}
