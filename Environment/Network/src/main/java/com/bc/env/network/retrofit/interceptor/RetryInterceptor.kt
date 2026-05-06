package com.bc.env.network.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

internal class RetryInterceptor(
    private val maxAttempts: Int = 3,
    private val retryInterval: Long = 1000L
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var lastException: IOException? = null

        for (attempt in 1..maxAttempts) {
            try {
                val response = chain.proceed(request)

                if (response.isSuccessful) {
                    return response
                }

                if (attempt == maxAttempts) {
                    return response
                }

                response.close()

                sleepBeforeRetry()
            } catch (e: IOException) {
                lastException = e

                if (attempt < maxAttempts) {
                    sleepBeforeRetry()
                }
            }
        }

        throw lastException ?: IOException("Unknown error during retry")
    }

    private fun sleepBeforeRetry() {
        try {
            Thread.sleep(retryInterval)
        } catch (_: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }
}