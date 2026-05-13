package com.bc.feature.detail.data.source

import com.bc.env.network.datasource.BaseDataSource
import com.bc.env.network.request.LoadParams
import com.bc.env.network.util.DomainProvider
import com.bc.core.data.model.PhotoItemDto
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

class PhotoDetailDataSource : BaseDataSource<PhotoItemDto>() {
    companion object {
        const val KEY_ID = "id"
    }

    override val domain: String = DomainProvider.unsplash
    override val path: String = Service.PATH

    override fun createCall(
        retrofit: Retrofit,
        params: LoadParams?
    ): Call<PhotoItemDto> {
        val id = params?.get(KEY_ID) as? String
        return retrofit.create(Service::class.java).call(id.orEmpty())
    }

    private interface Service {
        companion object {
            const val PATH = "/photos/{id}"
        }

        @GET(PATH)
        fun call(@Path(value = "id") id: String): Call<PhotoItemDto>
    }
}
