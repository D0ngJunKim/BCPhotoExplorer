package com.bc.feature.detail.presentation.unit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.bc.core.presentation.ui.ListSpan
import com.bc.core.presentation.ui.UiItem
import com.bc.feature.R
import com.bc.feature.detail.presentation.unit.mapper.toPhotographerUiItem
import com.bc.feature.detail.presentation.unit.preview.PhotoDetailPreviewData
import com.bc.feature.detail.presentation.vm.intent.PhotoDetailIntent
import com.ssg.env.ds.composite.LocalImage
import com.ssg.env.ds.composite.LocalText
import com.ssg.env.ds.foundation.RadiusToken
import com.ssg.env.ds.foundation.SpaceToken
import com.ssg.env.ds.foundation.background
import com.ssg.env.ds.foundation.clip
import com.ssg.env.ds.foundation.padding
import com.ssg.env.ds.foundation.spacedBy

data class PhotoDetailPhotographerUiItem(
    private val profileImgUrl: String?,
    private val name: String?,
    private val location: String?,
    private val bio: String?,
    private val instagramUsername: String?,
    private val portfolioUrl: String?,
    private val totalLikes: String,
    private val totalPhotos: String,
) : UiItem<PhotoDetailIntent>(ListSpan.FULL_FOR_ALL) {

    @Composable
    override fun SetItem(processIntent: (PhotoDetailIntent) -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(SpaceToken.XS)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colorResource(R.color.gray300))
            )

            Profile(
                profileImgUrl = profileImgUrl,
                name = name,
                location = location
            )

            ContactInfo(
                instagramUsername = instagramUsername,
                portfolioUrl = portfolioUrl
            )

            if (!bio.isNullOrBlank()) {
                LocalText(
                    text = bio,
                    color = colorResource(R.color.gray900),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(SpaceToken.XXS)
            ) {
                Chip(
                    icon = painterResource(R.drawable.ico_hearts),
                    value = totalLikes,
                    color = colorResource(R.color.gray900),
                    modifier = Modifier
                        .background(colorResource(R.color.gray200), RadiusToken.Circle)
                        .padding(SpaceToken.XXS)
                )
                Chip(
                    icon = painterResource(R.drawable.ico_camera),
                    value = totalPhotos,
                    color = colorResource(R.color.gray900),
                    modifier = Modifier
                        .background(colorResource(R.color.gray200), RadiusToken.Circle)
                        .padding(SpaceToken.XXS)
                )
            }
        }
    }

    @Composable
    fun ContactInfo(
        instagramUsername: String?,
        portfolioUrl: String?
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = SpaceToken.XXXS),
            verticalArrangement = Arrangement.spacedBy(SpaceToken.XXXS)
        ) {
            if (!instagramUsername.isNullOrBlank()) {
                Chip(
                    icon = painterResource(R.drawable.ico_instagram),
                    value = instagramUsername,
                    color = colorResource(R.color.gray900)
                )
            }
            if (!portfolioUrl.isNullOrBlank()) {
                LinkChip(
                    icon = painterResource(R.drawable.ico_link),
                    value = portfolioUrl,
                    color = colorResource(R.color.gray900)
                )
            }
        }
    }

    @Composable
    private fun Profile(
        profileImgUrl: String?,
        name: String?,
        location: String?
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SpaceToken.XS)
        ) {
            AsyncImage(
                model = profileImgUrl,
                placeholder = painterResource(R.drawable.ico_user),
                error = painterResource(R.drawable.ico_user),
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
                    .clip(RadiusToken.Circle)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(SpaceToken.XXXS)
            ) {
                LocalText(
                    text = name.orEmpty(),
                    color = colorResource(R.color.gray900),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                location?.let { location ->
                    Chip(
                        icon = painterResource(R.drawable.ico_marker_pin),
                        value = location,
                        color = colorResource(R.color.gray700)
                    )
                }
            }
        }
    }
}

@Composable
private fun Chip(
    icon: Painter,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SpaceToken.XXXS)
    ) {
        LocalImage(
            painter = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(color),
            modifier = Modifier.size(12.dp)
        )
        LocalText(
            text = value,
            color = color,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun LinkChip(
    icon: Painter,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val linkString = remember(value) {
        buildAnnotatedString {
            pushLink(
                LinkAnnotation.Url(
                    url = value,
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            textDecoration = TextDecoration.Underline
                        )
                    )
                )
            )
            append(value)
            pop()
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(SpaceToken.XXXS)
    ) {
        var lineBottom by remember { mutableFloatStateOf(0f) }

        LocalImage(
            painter = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(color),
            modifier = Modifier
                .size(12.dp)
                .offset {
                    val y = ((lineBottom.toDp() - 12.dp) / 2f).roundToPx()
                    IntOffset(0, y)
                }

        )
        LocalText(
            text = linkString,
            color = color,
            fontSize = 13.sp,
            onTextLayout = {
                lineBottom = it.getLineBottom(0)
            }
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
private fun Preview() {
    PhotoDetailPreviewData.photo.toPhotographerUiItem()?.BuildItem {}
}
