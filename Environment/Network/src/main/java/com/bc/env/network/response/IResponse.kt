package com.bc.env.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

private const val RES_CODE_SUCCESS_200 = "200"

@Keep
sealed interface IResponse {
    val responseCode: String?
    val responseMessage: String?
    val isSuccessful: Boolean

    @Keep
    class Unsplash : IResponse {
        @SerializedName("res_code")
        private val resCode: String? = null

        @SerializedName("res_message")
        private val resMessage: String? = null

        override val responseCode: String?
            get() = resCode
        override val responseMessage: String?
            get() = resMessage
        override val isSuccessful: Boolean
            get() = responseCode == RES_CODE_SUCCESS_200
    }
}