package com.bc.feature.main.photolist.data.model

import com.bc.core.domain.model.PhotoItemModel
import com.bc.core.domain.model.PhotoItemUserModel

fun PhotoItemDto.toDomain(): PhotoItemModel? {
    if (id.isNullOrEmpty()) return null
    if (width == null || width <= 0) return null
    if (height == null || height <= 0) return null
    val imageUrl = urls?.small_s3  ?: return null

    return PhotoItemModel(
        id = id,
        imageUrl = imageUrl,
        width = width,
        height = height,
        primaryColor = color,
        blurHash = blur_hash,
        description = description,
        altDescription = alt_description,
        trackDownloadUrl = link?.download_location,
        user = user?.toDomain()
    )
}

private fun PhotoItemUserDto.toDomain() = PhotoItemUserModel(
    id = id,
    profileImageUrl = profile_image?.small,
    username = username,
    name = name,
    instagramUsername = instagram_username,
    portfolioUrl = portfolio_url,
    bio = bio,
    location = location,
    totalLikes = total_likes ?: 0,
    totalPhotos = total_photos ?: 0,
)
