package com.bc.env.network.retrofit.interceptor

import com.bc.env.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

internal class UnsplashInterceptor : Interceptor {
    private val accessKey: String = BuildConfig.UNSPLASH_ACCESS_KEY

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request()
            .newBuilder()
            .header("Accept-Version", "v1")

        requestBuilder.header("Authorization", "Client-ID $accessKey")

        return chain.proceed(requestBuilder.build())
    }
}
