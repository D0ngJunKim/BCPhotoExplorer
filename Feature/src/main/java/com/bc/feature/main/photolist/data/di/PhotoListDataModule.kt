package com.bc.feature.main.photolist.data.di

import com.bc.feature.main.photolist.data.source.PhotoListDataSource
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