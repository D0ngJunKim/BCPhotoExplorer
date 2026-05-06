package com.bc.env.network.datasource

import com.bc.env.network.request.LoadParams
import com.bc.env.network.response.IResponse
import timber.log.Timber

abstract class BaseDataSource<DataModel> : IDataSource<LoadParams, DataModel>
        where DataModel : IResponse {

    suspend fun load(params: LoadParams): Result<DataModel> {
        return try {
            val body = DataSourceCallExecutor.execute(
                call = DataSourceCallExecutor.createCall(this, params)
            )

            Result.success(body)
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure(e)
        }
    }
}