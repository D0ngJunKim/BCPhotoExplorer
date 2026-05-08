package com.bc.env.network.constants

import timber.log.Timber

sealed class NetworkException(message: String?) : Exception(message) {

    init {
        Timber.tag("NetworkModule").e(message)
    }

    class NetworkFailure(responseCode: Int?) : NetworkException("네트워크 통신 중 오류가 발생했습니다. : $responseCode")
    class EmptyBody : NetworkException("Body가 Null입니다.")
}