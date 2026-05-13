package com.bc.feature.detail.presentation.unit.mapper

import androidx.compose.ui.unit.Dp
import com.bc.core.domain.model.PhotoItemModel
import com.bc.core.presentation.ui.UiItem
import com.bc.core.presentation.util.addNotNull
import com.bc.feature.detail.presentation.unit.PhotoDetailMetaUiItem
import com.bc.feature.detail.presentation.unit.PhotoDetailParallaxSpacerUiItem
import com.bc.feature.detail.presentation.unit.PhotoDetailPhotographerUiItem
import com.bc.feature.detail.presentation.unit.PhotoDetailSummaryUiItem
import com.bc.feature.detail.presentation.vm.intent.PhotoDetailIntent
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun List<UiItem<PhotoDetailIntent>>.withParallaxSpacing(height: Dp): List<UiItem<PhotoDetailIntent>> {
    return listOf(PhotoDetailParallaxSpacerUiItem(height = height)) + this
}

fun PhotoItemModel.toPhotoDetailList(): List<UiItem<PhotoDetailIntent>> {
    return buildList {
        addNotNull(toSummaryUiItem())
        addNotNull(toMetaUiItem())
        addNotNull(toPhotographerUiItem())
    }
}

fun PhotoItemModel.toSummaryUiItem(): PhotoDetailSummaryUiItem? {
    val description = description.orEmpty()
    val altDescription = altDescription.orEmpty()

    if (description.isEmpty() && altDescription.isEmpty()) return null

    return PhotoDetailSummaryUiItem(
        description = description,
        altDescription = altDescription
    )
}

fun PhotoItemModel.toMetaUiItem(): PhotoDetailMetaUiItem {
    var updatedDate = ""
    if (!updatedAt.isNullOrEmpty()) {
        val dataFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val date = dataFormat.parse(updatedAt)
        if (date != null) {
            updatedDate = SimpleDateFormat("yyyy년 M월 d일에 게시됨", Locale.KOREA).format(date)
        }
    }

    return PhotoDetailMetaUiItem(
        width = width,
        height = height,
        cameraModel = exif?.name.orEmpty(),
        updatedAt = updatedDate,
    )
}

fun PhotoItemModel.toPhotographerUiItem(): PhotoDetailPhotographerUiItem? {
    return user?.let {
        val totalLikes = try {
            NumberFormat.getInstance(Locale.KOREA).format(user.totalLikes).orEmpty()
        } catch (_: Exception) {
            user.totalLikes.toString()
        }

        val totalPhotos = try {
            NumberFormat.getInstance(Locale.KOREA).format(user.totalPhotos).orEmpty()
        } catch (_: Exception) {
            user.totalPhotos.toString()
        }

        PhotoDetailPhotographerUiItem(
            profileImgUrl = user.profileImageUrl,
            name = user.name,
            location = user.location,
            bio = user.bio,
            instagramUsername = user.instagramUsername,
            portfolioUrl = user.portfolioUrl,
            totalLikes = totalLikes,
            totalPhotos = totalPhotos,
        )
    }
}
