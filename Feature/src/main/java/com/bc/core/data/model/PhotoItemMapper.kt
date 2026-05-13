package com.bc.core.data.model

import com.bc.core.domain.model.PhotoItemModel
import com.bc.core.domain.model.PhotoItemExifModel
import com.bc.core.domain.model.PhotoItemLinksModel
import com.bc.core.domain.model.PhotoItemLocationModel
import com.bc.core.domain.model.PhotoItemLocationPositionModel
import com.bc.core.domain.model.PhotoItemTagModel
import com.bc.core.domain.model.PhotoItemUserModel

fun PhotoItemDto.toDomain(): PhotoItemModel? {
    if (id.isNullOrEmpty()) return null
    if (width == null || width <= 0) return null
    if (height == null || height <= 0) return null
    val imageUrl = urls?.regular  ?: return null

    return PhotoItemModel(
        id = id,
        updatedAt = updated_at,
        imageUrl = imageUrl,
        width = width,
        height = height,
        primaryColor = color,
        blurHash = blur_hash,
        description = description,
        altDescription = alt_description,
        trackDownloadUrl = links?.download_location,
        user = user?.toDomain(),
        links = links?.toDomain(),
        exif = exif?.toDomain(),
        location = location?.toDomain(),
        tags = tags.orEmpty().map { it.toDomain() }
    )
}

private fun PhotoItemUserDto.toDomain() = PhotoItemUserModel(
    id = id,
    profileImageUrl = profile_image?.medium,
    username = username,
    name = name,
    instagramUsername = instagram_username,
    portfolioUrl = portfolio_url,
    bio = bio,
    location = location,
    totalLikes = total_likes ?: 0,
    totalPhotos = total_photos ?: 0,
)

private fun PhotoItemLinkDto.toDomain() = PhotoItemLinksModel(
    self = self,
    html = html,
    download = download,
    downloadLocation = download_location
)

private fun PhotoItemExifDto.toDomain() = PhotoItemExifModel(
    make = make,
    model = model,
    name = name,
    exposureTime = exposure_time,
    aperture = aperture,
    focalLength = focal_length,
    iso = iso
)

private fun PhotoItemLocationDto.toDomain() = PhotoItemLocationModel(
    name = name,
    city = city,
    country = country,
    position = position?.toDomain()
)

private fun PhotoItemLocationPositionDto.toDomain() = PhotoItemLocationPositionModel(
    latitude = latitude,
    longitude = longitude
)

private fun PhotoItemTagDto.toDomain() = PhotoItemTagModel(
    title = title,
    type = type
)
