package com.bc.env.network.datasource

import android.webkit.URLUtil
import com.bc.env.network.constants.NetworkException
import com.bc.env.network.request.IParams
import com.bc.env.network.response.IResponse
import com.bc.env.network.retrofit.RetrofitFactory
import retrofit2.Call
import retrofit2.awaitResponse

internal object DataSourceCallExecutor {
    fun <Params : IParams, DataModel : IResponse> createCall(
        dataSource: IDataSource<Params, DataModel>,
        params: Params?
    ): Call<DataModel> {
        var baseUrl = dataSource.domain.trim()
        if (!URLUtil.isNetworkUrl(baseUrl)) {
            baseUrl = "https://$baseUrl"
        }
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/"
        }

        val retrofit = RetrofitFactory.get(
            baseUrl = baseUrl,
            path = dataSource.path,
            config = dataSource.config,
            setupGson = dataSource::setupGson,
            setupOkHttp = dataSource::setupOkHttp
        )

        return dataSource.createCall(retrofit, params)
    }

    suspend fun <DataModel : IResponse> execute(call: Call<DataModel>): DataModel {
        try {
            val response = call.awaitResponse()

            if (!response.isSuccessful) {
                throw NetworkException.NetworkFailure(response.code())
            }

            val body = response.body() ?: throw NetworkException.EmptyBody()

            return body
        } catch (e: Exception) {
            throw e
        }
    }
}
