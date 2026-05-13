package com.bc.feature.detail.data.di

import com.bc.feature.detail.data.repository.PhotoDetailRepositoryImpl
import com.bc.feature.detail.data.source.PhotoDetailDataSource
import com.bc.feature.detail.domain.repository.PhotoDetailRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object PhotoDetailDataSourceModule {
    @Provides
    fun provideDataSource(): PhotoDetailDataSource = PhotoDetailDataSource()
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class PhotoDetailRepositoryModule {
    @Binds
    abstract fun bindRepository(repository: PhotoDetailRepositoryImpl): PhotoDetailRepository
}
