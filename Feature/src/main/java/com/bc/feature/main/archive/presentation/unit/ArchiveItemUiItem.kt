package com.bc.feature.main.archive.presentation.unit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.bc.core.domain.model.PhotoItemModel
import com.bc.core.domain.model.PhotoItemUserModel
import com.bc.core.presentation.ui.AsyncImageBlurHash
import com.bc.core.presentation.ui.LikeButton
import com.bc.core.presentation.ui.ListSpan
import com.bc.core.presentation.ui.UiItem
import com.bc.env.nav.LocalGlobalNavigator
import com.bc.feature.detail.presentation.DetailRoute
import com.bc.feature.main.archive.presentation.unit.mapper.toArchiveItem
import com.bc.feature.main.archive.presentation.vm.intent.ArchiveListIntent
import com.ssg.env.ds.composite.LocalText
import com.ssg.env.ds.foundation.RadiusToken
import com.ssg.env.ds.foundation.SpaceToken
import com.ssg.env.ds.foundation.background
import com.ssg.env.ds.foundation.clip
import com.ssg.env.ds.foundation.padding
import com.ssg.env.ds.foundation.spacedBy
import kotlin.math.roundToInt

data class ArchiveItemUiItem(
    val id: String,
    val imageUrl: String,
    val width: Int,
    val height: Int,
    val ratio: Float,
    val primaryColor: Color?,
    val textColor: Color,
    val blurHash: String?,
    val description: String?,
    val altDescription: String?,
    val trackDownloadUrl: String?,
    val profileImageUrl: String?,
    val photographer: String?,
    val origin: PhotoItemModel
) : UiItem<ArchiveListIntent>(ListSpan.SINGLE_FOR_ALL) {
    override val itemKey: String = id

    @Composable
    override fun SetItem(processIntent: ((ArchiveListIntent) -> Unit)) {
        val primaryColor = primaryColor ?: Color.Black
        val navigator = LocalGlobalNavigator.current

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = {
                        navigator?.navigate(DetailRoute(origin))
                    })
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RadiusToken.MD)
            ) {
                val density = LocalDensity.current
                val width = with(density) { maxWidth.roundToPx() }
                val height = (width / ratio).roundToInt()

                AsyncImageBlurHash(
                    model = imageUrl,
                    blurHash = blurHash,
                    contentDescription = altDescription,
                    width = width,
                    height = height,
                    primaryColor = primaryColor.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ratio)
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .wrapContentWidth()
                    .padding(horizontal = SpaceToken.XXXS, vertical = SpaceToken.XXXS)
                    .background(primaryColor, RadiusToken.MD)
                    .padding(horizontal = SpaceToken.XXS, vertical = SpaceToken.XXXS),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SpaceToken.XXXS)
            ) {
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color.White, RadiusToken.Circle)
                        .clip(RadiusToken.Circle)
                )

                Column {
                    if (!photographer.isNullOrEmpty()) {
                        LocalText(
                            text = photographer,
                            color = textColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }

                    if (!description.isNullOrEmpty()) {
                        LocalText(
                            text = description,
                            color = textColor,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Normal,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
            }

            LikeButton(
                onClick = {
                    processIntent(ArchiveListIntent.OnToggleLike(origin))
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(all = SpaceToken.XXXS),
                selected = true
            )
        }
    }
}

@Composable
@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
private fun Preview(@PreviewParameter(DataProvider::class) data: PhotoItemModel) {
    data.toArchiveItem().BuildItem { }
}

private class DataProvider : PreviewParameterProvider<PhotoItemModel> {
    override val values = sequenceOf(
        PhotoItemModel(
            id = "YZZgAMftFuQ",
            updatedAt = "2023-09-20T14:20:00Z",
            imageUrl = "https://images.unsplash.com/photo-1773062189964-75a471b19934?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w5Mzk3NDl8MHwxfGFsbHwzfHx8fHx8fHwxNzc4MDQ5NTI5fA&ixlib=rb-4.1.0&q=80&w=1080",
            width = 3400,
            height = 2267,
            primaryColor = "#59260c",
            blurHash = $$"LHEBmV]$D%1OwJnNT0Xms9JUa_NH",
            description = "Vinyl Record Player (IG: @clay.banks)",
            altDescription = "A blue record player with a record on it",
            trackDownloadUrl = "https://api.unsplash.com/photos/YZZgAMftFuQ/download?ixid=sample",
            user = PhotoItemUserModel(
                id = "rUXhgOTUmb0",
                profileImageUrl = "https://images.unsplash.com/profile-1670236743900-356b1ee0dc42image?ixlib=rb-4.1.0&crop=faces&fit=crop&w=32&h=32",
                username = "claybanks",
                name = "Clay Banks",
                instagramUsername = "clay.banks",
                portfolioUrl = "http://claybanks.info",
                bio = "\uD83D\uDCF7 Freelance Photog\\r\\n\uD83D\uDCCD Catskill Mountains NY   \uD83C\uDF0E Presets & Prints \uD83D\uDC49\uD83C\uDFFD https://claybanks.info  If you use my images and would like to say thanks, feel free to donate via the PayPal link on my profile",
                location = "New York",
                totalLikes = 513,
                totalPhotos = 1865
            ),
        )
    )
}
