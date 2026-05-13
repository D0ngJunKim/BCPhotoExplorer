package com.bc.feature.detail.domain.repository

import com.bc.core.domain.model.PhotoItemModel

interface PhotoDetailRepository {
    suspend fun loadApi(id: String): Result<PhotoItemModel>
}
