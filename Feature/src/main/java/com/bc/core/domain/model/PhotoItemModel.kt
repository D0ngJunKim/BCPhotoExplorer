package com.bc.core.domain.model


data class PhotoItemModel(
    val id: String,
    val imageUrl: String,
    val width: Int,
    val height: Int,
    val primaryColor: String?,
    val blurHash: String?,
    val description: String?,
    val altDescription: String?,
    val trackDownloadUrl: String?,
    val user: PhotoItemUserModel?
)

data class PhotoItemUserModel(
    val id: String?,
    val profileImageUrl: String?,
    val username: String?,
    val name: String?,
    val instagramUsername: String?,
    val portfolioUrl: String?,
    val bio: String?,
    val location: String?,
    val totalLikes: Int,
    val totalPhotos: Int
)