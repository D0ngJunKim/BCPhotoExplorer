package com.bc.feature.main.photolist.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.bc.env.network.request.PagingLoadParams
import com.bc.feature.main.photolist.data.source.PhotoListDataSource
import javax.inject.Inject
import javax.inject.Provider

class PhotoListRepository @Inject constructor(
    private val dataSourceProvider: Provider<PhotoListDataSource>
) {
    fun getPhotoList() = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 10
        ),
        initialKey = PagingLoadParams(page = 1),
        pagingSourceFactory = { dataSourceProvider.get() }
    ).flow
}