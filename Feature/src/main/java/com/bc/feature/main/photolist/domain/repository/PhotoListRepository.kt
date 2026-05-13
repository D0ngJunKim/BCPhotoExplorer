package com.bc.feature.main.photolist.domain.repository

import androidx.paging.PagingData
import com.bc.core.domain.model.PhotoItemModel
import kotlinx.coroutines.flow.Flow

interface PhotoListRepository {
    fun getPhotoList(): Flow<PagingData<PhotoItemModel>>
}
