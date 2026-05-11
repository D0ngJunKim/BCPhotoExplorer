package com.bc.core.data.archive.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection")
data class CollectionEntity(
    @PrimaryKey val id: String,
    val imagePath: String,
    val width: Int,
    val height: Int,
    val description: String?,
    val contentDescription: String?,
    val primaryColor: String?,
    val blurHash: String?,
    val trackDownloadUrl: String?,
    @Embedded(prefix = "photographer_")
    val photographer: PhotographerEntity
)

data class PhotographerEntity(
    val id: String,
    val name: String,
    val userName: String,
    val profileUrl: String?,
    val portfolioUrl: String?,
    val instagramUsername: String?,
    val totalPhotos: Int,
    val totalLikes: Int,
    val bio: String?,
    val location: String?
)
