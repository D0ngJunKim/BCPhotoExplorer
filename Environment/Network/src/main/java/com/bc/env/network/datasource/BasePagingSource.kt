package com.bc.env.network.datasource

import androidx.paging.PagingSource
import com.bc.env.network.constants.NetworkException
import com.bc.env.network.request.PagingLoadParams
import okhttp3.Headers

abstract class BasePagingSource<DataModel, DomainModel> : PagingSource<PagingLoadParams, DomainModel>(), IDataSource<PagingLoadParams, DataModel>
        where DataModel : Any, DomainModel : Any {
    protected abstract fun hasNextPage(params: PagingLoadParams?, headers: Headers, body: DataModel): Boolean
    protected abstract fun mapToDomain(params: PagingLoadParams?, body: DataModel): List<DomainModel>

    override suspend fun load(params: LoadParams<PagingLoadParams>): LoadResult<PagingLoadParams, DomainModel> {
        return try {
            val requestParams = params.key
            val page = requestParams?.page ?: 1

            val response = DataSourceCallExecutor.execute(
                call = DataSourceCallExecutor.createCall(this, requestParams)
            )
            val headers = response.headers()
            val body = response.body() ?: throw NetworkException.EmptyBody()

            val hasNext = hasNextPage(requestParams, headers, body)

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
