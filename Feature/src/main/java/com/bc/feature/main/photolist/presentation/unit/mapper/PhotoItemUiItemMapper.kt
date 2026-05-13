package com.bc.feature.main.photolist.presentation.unit.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.bc.core.domain.model.PhotoItemModel
import com.bc.core.presentation.util.toComposeColorOrNull
import com.bc.feature.main.photolist.presentation.unit.PhotoItemUiItem

fun PhotoItemModel.toPhotoItem(isArchived: Boolean = false): PhotoItemUiItem {
    val ratio = width.toFloat() / height.toFloat()
    val primaryColor = primaryColor?.toComposeColorOrNull()
    val textColor = if (primaryColor != null) {
        if (ColorUtils.calculateLuminance(primaryColor.toArgb()) > 0.5f) {
            Color.Black
        } else {
            Color.White
        }
    } else {
        Color.White
    }

    return PhotoItemUiItem(
        id = id,
        imageUrl = imageUrl,
        width = width,
        height = height,
        ratio = ratio,
        primaryColor = primaryColor,
        textColor = textColor,
        blurHash = blurHash,
        description = description,
        altDescription = altDescription,
        trackDownloadUrl = trackDownloadUrl,
        profileImageUrl = user?.profileImageUrl,
        photographer = user?.name,
        isArchived = isArchived,
        origin = this
    )
}
