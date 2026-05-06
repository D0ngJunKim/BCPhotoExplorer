package com.bc.env.network.datasource

import androidx.paging.PagingSource
import com.bc.env.network.request.PagingLoadParams
import com.bc.env.network.response.IResponse

abstract class BasePagingSource<DataModel, DomainModel> : PagingSource<PagingLoadParams, DomainModel>(), IDataSource<PagingLoadParams, DataModel>
        where DataModel : IResponse, DomainModel : Any {
    protected abstract fun hasNextPage(params: PagingLoadParams?, body: DataModel): Boolean
    protected abstract fun mapToDomain(params: PagingLoadParams?, body: DataModel): List<DomainModel>

    override suspend fun load(params: LoadParams<PagingLoadParams>): LoadResult<PagingLoadParams, DomainModel> {
        return try {
            val requestParams = params.key
            val page = requestParams?.page ?: 1

            val body = DataSourceCallExecutor.execute(
                call = DataSourceCallExecutor.createCall(this, requestParams)
            )

            val hasNext = hasNextPage(requestParams, body)

            LoadResult.Page(
                data = mapToDomain(requestParams, body),
                prevKey = if (page == 1) null else requestParams?.decrement(),
                nextKey = if (hasNext) requestParams?.increment() else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
