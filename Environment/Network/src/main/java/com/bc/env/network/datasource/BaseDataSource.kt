package com.bc.env.network.datasource

import com.bc.env.network.constants.NetworkException
import com.bc.env.network.request.LoadParams
import retrofit2.awaitResponse
import timber.log.Timber

abstract class BaseDataSource<DataModel> : IDataSource<LoadParams, DataModel>
        where DataModel : Any {

    suspend fun load(params: LoadParams): Result<DataModel> {
        return try {
            val body = DataSourceCallExecutor.execute(
                call = DataSourceCallExecutor.createCall(this, params)
            ).body() ?: throw NetworkException.EmptyBody()

            Result.success(body)
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure(e)
        }
    }
}