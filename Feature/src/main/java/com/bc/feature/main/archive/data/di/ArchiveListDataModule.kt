package com.bc.feature.main.archive.data.di

import com.bc.feature.main.archive.data.repository.ArchiveListRepositoryImpl
import com.bc.feature.main.archive.domain.repository.ArchiveListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ArchiveListRepositoryModule {
    @Binds
    abstract fun bindRepository(
        repository: ArchiveListRepositoryImpl
    ): ArchiveListRepository
}
