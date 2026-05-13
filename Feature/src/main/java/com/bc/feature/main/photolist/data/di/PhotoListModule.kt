package com.bc.feature.main.photolist.data.di

import com.bc.feature.main.photolist.data.repository.PhotoListRepositoryImpl
import com.bc.feature.main.photolist.data.source.PhotoListDataSource
import com.bc.feature.main.photolist.domain.repository.PhotoListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object PhotoListDataModule {
    @Provides
    fun provideDataSource(): PhotoListDataSource = PhotoListDataSource()
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class PhotoListRepositoryModule {
    @Binds
    abstract fun bindRepository(
        repository: PhotoListRepositoryImpl
    ): PhotoListRepository
}
