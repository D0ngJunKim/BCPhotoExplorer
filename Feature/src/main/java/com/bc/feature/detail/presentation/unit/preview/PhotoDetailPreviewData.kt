package com.bc.feature.detail.presentation.unit.preview

import com.bc.core.domain.model.PhotoItemModel
import com.bc.core.domain.model.PhotoItemUserModel

object PhotoDetailPreviewData {
    val user = PhotoItemUserModel(
        id = "rUXhgOTUmb0",
        profileImageUrl = "https://images.unsplash.com/profile-1670236743900-356b1ee0dc42image?ixlib=rb-4.1.0&crop=faces&fit=crop&w=32&h=32",
        username = "claybanks",
        name = "Clay Banks",
        instagramUsername = "clay.banks",
        portfolioUrl = "http://claybanks.info",
        bio = "Freelance photographer based in Catskill Mountains, NY. Presets and prints available through portfolio.",
        location = "New York",
        totalLikes = 513,
        totalPhotos = 1865
    )

    val photo = PhotoItemModel(
        id = "YZZgAMftFuQ",
        updatedAt = "2026-05-12T04:52:38Z",
        imageUrl = "https://images.unsplash.com/photo-1773062189964-75a471b19934?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w5Mzk3NDl8MHwxfGFsbHwzfHx8fHx8fHwxNzc4MDQ5NTI5fA&ixlib=rb-4.1.0&q=80&w=1080",
        width = 3400,
        height = 2267,
        primaryColor = "#59260c",
        blurHash = "LHEBmV]\$D%1OwJnNT0Xms9JUa_NH",
        description = "Vinyl Record Player (IG: @clay.banks)",
        altDescription = "A blue record player with a record on it",
        trackDownloadUrl = "https://api.unsplash.com/photos/YZZgAMftFuQ/download?ixid=sample",
        user = user,
    )
}