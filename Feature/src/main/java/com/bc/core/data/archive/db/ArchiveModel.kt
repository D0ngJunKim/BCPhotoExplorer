package com.bc.core.data.archive.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [CollectionEntity::class], version = 3, exportSchema = false)
abstract class ArchiveDatabase : RoomDatabase() {
    abstract fun archiveDao(): ArchiveDao
}

@Module
@InstallIn(SingletonComponent::class)
object ArchiveModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ArchiveDatabase {
        return Room.databaseBuilder(context, ArchiveDatabase::class.java, "archive_db")
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideDao(db: ArchiveDatabase) = db.archiveDao()
}
