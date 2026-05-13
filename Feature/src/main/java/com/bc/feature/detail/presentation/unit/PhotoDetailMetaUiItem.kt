package com.bc.feature.detail.presentation.unit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bc.core.presentation.ui.ListSpan
import com.bc.core.presentation.ui.UiItem
import com.bc.feature.R
import com.bc.feature.detail.presentation.unit.mapper.toMetaUiItem
import com.bc.feature.detail.presentation.unit.preview.PhotoDetailPreviewData
import com.bc.feature.detail.presentation.vm.intent.PhotoDetailIntent
import com.ssg.env.ds.composite.LocalImage
import com.ssg.env.ds.composite.LocalText
import com.ssg.env.ds.foundation.RadiusToken
import com.ssg.env.ds.foundation.SpaceToken
import com.ssg.env.ds.foundation.background
import com.ssg.env.ds.foundation.padding
import com.ssg.env.ds.foundation.spacedBy

data class PhotoDetailMetaUiItem(
    private val width: Int,
    private val height: Int,
    private val cameraModel: String,
    private val updatedAt: String,
    private val tags: List<String>,
) : UiItem<PhotoDetailIntent>(ListSpan.FULL_FOR_ALL) {

    @Composable
    override fun SetItem(processIntent: (PhotoDetailIntent) -> Unit) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(SpaceToken.XXXS)
        ) {
            if (updatedAt.isNotEmpty()) {
                Chip(
                    icon = painterResource(R.drawable.ico_calendar_plus),
                    value = updatedAt
                )
            }
            if (cameraModel.isNotEmpty()) {
                Chip(
                    icon = painterResource(R.drawable.ico_camera),
                    value = cameraModel
                )
            }
            Chip(
                icon = painterResource(R.drawable.ico_maximize),
                value = "$width x $height"
            )

            if (tags.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = SpaceToken.XXS),
                    horizontalArrangement = Arrangement.spacedBy(SpaceToken.XXXS),
                    verticalArrangement = Arrangement.spacedBy(SpaceToken.XXXS),
                    maxLines = 2
                ) {
                    tags.forEach { tag ->
                        Chip(
                            icon = painterResource(R.drawable.ico_hash),
                            value = tag,
                            spacing = SpaceToken.Zero
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Chip(
    icon: Painter,
    value: String,
    modifier: Modifier = Modifier,
    spacing: SpaceToken = SpaceToken.XXXS
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        LocalImage(
            painter = icon,
            contentDescription = null,
            modifier = Modifier
                .size(12.dp)
        )

        LocalText(
            text = value,
            color = colorResource(R.color.gray900),
            fontSize = 12.sp,
        )
    }
}


@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
private fun Preview() {
    PhotoDetailPreviewData.photo.toMetaUiItem().BuildItem {}
}
