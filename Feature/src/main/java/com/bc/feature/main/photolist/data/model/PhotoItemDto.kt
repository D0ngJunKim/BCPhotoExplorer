@file:Suppress("PropertyName")

package com.bc.feature.main.photolist.data.model

data class PhotoItemDto(
    val id: String?,
    val created_at: String?,
    val updated_at: String?,
    val promoted_at: String?,
    val width: Int?,
    val height: Int?,
    val color: String?,
    val blur_hash: String?,
    val description: String?,
    val alt_description: String?,
    val urls: PhotoItemUrlDto?,
    val user: PhotoItemUserDto?,
    val links: PhotoItemLinkDto?
)

data class PhotoItemUrlDto(
    val raw: String?,
    val full: String?,
    val regular: String?,
    val small: String?,
    val thumb: String?,
    val small_s3: String?
)

data class PhotoItemUserDto(
    val id: String?,
    val updated_at: String?,
    val username: String?,
    val name: String?,
    val portfolio_url: String?,
    val bio: String?,
    val location: String?,
    val profile_image: PhotoItemUserProfileImageDto?,
    val instagram_username: String?,
    val total_likes: Int?,
    val total_photos: Int?,
)

data class PhotoItemUserProfileImageDto(
    val small: String?,
    val medium: String?,
    val large: String?
)

data class PhotoItemLinkDto(
    val self: String?,
    val html: String?,
    val download: String?,
    val download_location: String?
)