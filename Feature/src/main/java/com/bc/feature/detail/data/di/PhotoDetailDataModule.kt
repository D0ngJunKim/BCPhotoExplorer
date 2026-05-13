package com.bc.feature.detail.data.di

import com.bc.feature.detail.data.source.PhotoDetailDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object PhotoDetailDataModule {
    @Provides
    fun provideDataSource(): PhotoDetailDataSource = PhotoDetailDataSource()
}