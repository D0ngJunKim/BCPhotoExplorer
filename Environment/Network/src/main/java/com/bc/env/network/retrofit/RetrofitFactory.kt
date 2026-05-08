package com.bc.env.network.retrofit

import com.bc.env.network.datasource.DataSourceConfig
import com.bc.env.network.retrofit.interceptor.RetryInterceptor
import com.bc.env.network.retrofit.interceptor.UnsplashInterceptor
import com.bc.env.network.util.DomainProvider
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECTION_TIMEOUT = 40L
private const val READ_TIMEOUT = 40L
private const val WRITE_TIMEOUT = 40L

internal object RetrofitFactory {
    private val cache = mutableMapOf<String, Retrofit>()

    fun get(
        baseUrl: String,
        path: String,
        config: DataSourceConfig,
        setupGson: GsonBuilder.() -> GsonBuilder,
        setupOkHttp: OkHttpClient.Builder.() -> OkHttpClient.Builder
    ): Retrofit {
        synchronized(this) {
            val key = baseUrl + path + config.toString()

            return cache.getOrPut(key) {
                val gsonBuilder = GsonBuilder()
                    .setupGson()

                val okHttpClientBuilder = OkHttpClient.Builder()
                    .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .followRedirects(true)
                    .followSslRedirects(true)
                    .setupOkHttp()

                if (config.useRetry) {
                    okHttpClientBuilder.addInterceptor(RetryInterceptor())
                }

                if (baseUrl.contains(DomainProvider.unsplash)) {
                    okHttpClientBuilder.addInterceptor(UnsplashInterceptor())
                }

                Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .build()
            }
        }
    }
}