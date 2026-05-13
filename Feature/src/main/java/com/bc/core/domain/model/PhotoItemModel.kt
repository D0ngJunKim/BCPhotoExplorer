package com.bc.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PhotoItemModel(
    val id: String,
    val updatedAt: String?,
    val imageUrl: String,
    val width: Int,
    val height: Int,
    val primaryColor: String?,
    val blurHash: String?,
    val description: String?,
    val altDescription: String?,
    val trackDownloadUrl: String?,
    val user: PhotoItemUserModel?,
    val links: PhotoItemLinksModel? = null,
    val exif: PhotoItemExifModel? = null,
    val location: PhotoItemLocationModel? = null,
    val tags: List<PhotoItemTagModel> = emptyList()
)

@Serializable
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

@Serializable
data class PhotoItemLinksModel(
    val self: String?,
    val html: String?,
    val download: String?,
    val downloadLocation: String?
)

@Serializable
data class PhotoItemExifModel(
    val make: String?,
    val model: String?,
    val name: String?,
    val exposureTime: String?,
    val aperture: String?,
    val focalLength: String?,
    val iso: Int?
)

@Serializable
data class PhotoItemLocationModel(
    val name: String?,
    val city: String?,
    val country: String?,
    val position: PhotoItemLocationPositionModel?
)

@Serializable
data class PhotoItemLocationPositionModel(
    val latitude: Double?,
    val longitude: Double?
)

@Serializable
data class PhotoItemTagModel(
    val title: String?,
    val type: String?
)
