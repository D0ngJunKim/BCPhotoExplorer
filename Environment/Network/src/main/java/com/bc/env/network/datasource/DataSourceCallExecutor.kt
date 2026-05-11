package com.bc.env.network.datasource

import android.webkit.URLUtil
import com.bc.env.network.constants.NetworkException
import com.bc.env.network.request.IParams
import com.bc.env.network.retrofit.RetrofitFactory
import retrofit2.Call
import retrofit2.Response
import retrofit2.awaitResponse

internal object DataSourceCallExecutor {
    fun <Params : IParams, DataModel : Any> createCall(
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

    internal suspend fun <DataModel : Any> execute(call: Call<DataModel>): Response<DataModel> {
        try {
            val response = call.awaitResponse()

            if (!response.isSuccessful) {
                throw NetworkException.NetworkFailure(response.code())
            }

            return response
        } catch (e: Exception) {
            throw e
        }
    }
}
