package com.bc.env.network.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Response

internal class UnsplashInterceptor : Interceptor {
    // local.properties를 읽어서 처리하도록 하였으나 빌드 편의를 위해 키 값 하드코딩 처리.
//    private val accessKey: String = BuildConfig.UNSPLASH_ACCESS_KEY
    private val accessKey: String = "gn0ZjaM9arsUiEoM7g_B2JK5NGhyYxbdHE_vz8h9guk"

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request()
            .newBuilder()
            .header("Accept-Version", "v1")

        requestBuilder.header("Authorization", "Client-ID $accessKey")

        return chain.proceed(requestBuilder.build())
    }
}
