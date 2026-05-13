package com.bc.core.data.archive.di

import com.bc.core.data.archive.repostiory.ArchiveRepositoryImpl
import com.bc.core.domain.repository.ArchiveRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ArchiveRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRepository(
        repository: ArchiveRepositoryImpl
    ): ArchiveRepository
}
