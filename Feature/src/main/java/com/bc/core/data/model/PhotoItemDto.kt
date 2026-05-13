@file:Suppress("PropertyName")

package com.bc.core.data.model

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
    val links: PhotoItemLinkDto?,
    val exif: PhotoItemExifDto?,
    val location: PhotoItemLocationDto?,
    val tags: List<PhotoItemTagDto>?
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

data class PhotoItemExifDto(
    val make: String?,
    val model: String?,
    val name: String?,
    val exposure_time: String?,
    val aperture: String?,
    val focal_length: String?,
    val iso: Int?
)

data class PhotoItemLocationDto(
    val name: String?,
    val city: String?,
    val country: String?,
    val position: PhotoItemLocationPositionDto?
)

data class PhotoItemLocationPositionDto(
    val latitude: Double?,
    val longitude: Double?
)

data class PhotoItemTagDto(
    val title: String?,
    val type: String?
)