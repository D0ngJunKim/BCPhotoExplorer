package com.bc.core.data.archive.source

import com.bc.core.data.archive.model.TrackDownloadDto
import com.bc.env.network.datasource.BaseDataSource
import com.bc.env.network.request.LoadParams
import com.bc.env.network.util.DomainProvider
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Url
import javax.inject.Inject

class TrackDownloadDataSource @Inject constructor() : BaseDataSource<TrackDownloadDto>() {
    companion object {
        const val KEY_DOWNLOAD_LOCATION = "download_location"
    }

    override val domain: String = DomainProvider.unsplash
    override var path: String = Service.PATH

    override fun createCall(
        retrofit: Retrofit,
        params: LoadParams?,
        pageSize: Int
    ): Call<TrackDownloadDto> {
        val url = params?.get(KEY_DOWNLOAD_LOCATION) as? String
        return retrofit.create(Service::class.java).call(url.orEmpty())
    }

    private interface Service {
        companion object {
            const val PATH = "/"
        }

        @GET
        fun call(@Url url: String): Call<TrackDownloadDto>
    }
}

