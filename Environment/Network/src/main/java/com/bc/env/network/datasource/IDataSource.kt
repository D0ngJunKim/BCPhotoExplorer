package com.bc.env.network.datasource

import com.bc.env.network.request.IParams
import com.bc.env.network.response.IResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit

internal sealed interface IDataSource<Params, DataModel>
        where Params : IParams, DataModel : IResponse {
    val domain: String
    val path: String
    val config: DataSourceConfig
        get() = DataSourceConfig()

    fun createCall(retrofit: Retrofit, params: Params?): Call<DataModel>

    fun setupGson(builder: GsonBuilder): GsonBuilder = builder

    fun setupOkHttp(builder: OkHttpClient.Builder): OkHttpClient.Builder = builder
}
