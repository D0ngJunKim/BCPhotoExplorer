package com.bc.core.data.archive.db

import com.bc.core.domain.model.PhotoItemModel
import com.bc.core.domain.model.PhotoItemUserModel

fun PhotoItemModel.toCollectionEntity(imagePath: String = imageUrl): CollectionEntity {
    return CollectionEntity(
        id = id,
        imagePath = imagePath,
        width = width,
        height = height,
        description = description,
        contentDescription = altDescription,
        primaryColor = primaryColor,
        blurHash = blurHash,
        trackDownloadUrl = trackDownloadUrl,
        photographer = PhotographerEntity(
            id = user?.id.orEmpty(),
            name = user?.name.orEmpty(),
            userName = user?.username.orEmpty(),
            profileUrl = user?.profileImageUrl,
            portfolioUrl = user?.portfolioUrl,
            instagramUsername = user?.instagramUsername,
            totalPhotos = user?.totalPhotos ?: 0,
            totalLikes = user?.totalLikes ?: 0,
            bio = user?.bio,
            location = user?.location
        )
    )
}

fun CollectionEntity.toDomain(): PhotoItemModel {
    return PhotoItemModel(
        id = id,
        imageUrl = imagePath,
        width = width,
        height = height,
        primaryColor = primaryColor,
        blurHash = blurHash,
        description = description,
        altDescription = contentDescription,
        trackDownloadUrl = trackDownloadUrl,
        user = PhotoItemUserModel(
            id = photographer.id,
            profileImageUrl = photographer.profileUrl,
            username = photographer.userName,
            name = photographer.name,
            instagramUsername = photographer.instagramUsername,
            portfolioUrl = photographer.portfolioUrl,
            bio = photographer.bio,
            location = photographer.location,
            totalLikes = photographer.totalLikes,
            totalPhotos = photographer.totalPhotos
        )
    )
}
