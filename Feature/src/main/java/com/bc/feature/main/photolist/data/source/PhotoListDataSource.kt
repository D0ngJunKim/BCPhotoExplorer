package com.bc.feature.main.photolist.data.source

import androidx.paging.PagingState
import com.bc.core.domain.model.PhotoItemModel
import com.bc.env.network.datasource.BasePagingSource
import com.bc.env.network.request.PagingLoadParams
import com.bc.env.network.request.Parameters
import com.bc.env.network.util.DomainProvider
import com.bc.core.data.model.PhotoItemDto
import com.bc.core.data.model.toDomain
import okhttp3.Headers
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.QueryMap

class PhotoListDataSource : BasePagingSource<List<PhotoItemDto>, PhotoItemModel>() {
    override fun hasNextPage(
        params: PagingLoadParams?,
        headers: Headers,
        body: List<PhotoItemDto>
    ): Boolean {
        headers["Link"]?.split(",")?.forEach { link ->
            if (link.contains("rel=\"next\"")) {
                return true
            }
        }
        return false
    }

    override fun mapToDomain(
        params: PagingLoadParams?,
        body: List<PhotoItemDto>
    ): List<PhotoItemModel> {
        return body.mapNotNull { it.toDomain() }
    }

    override fun getRefreshKey(state: PagingState<PagingLoadParams, PhotoItemModel>): PagingLoadParams? {
        return PagingLoadParams(1)
    }

    override val domain: String = DomainProvider.unsplash

    override val path: String = Service.PATH

    override fun createCall(
        retrofit: Retrofit,
        params: PagingLoadParams?
    ): Call<List<PhotoItemDto>> {
        return retrofit.create(Service::class.java).call(params?.toMap())
    }

    private interface Service {
        companion object {
            const val PATH = "/photos"
        }

        @GET(PATH)
        fun call(@QueryMap queries: Parameters?): Call<List<PhotoItemDto>>
    }
}