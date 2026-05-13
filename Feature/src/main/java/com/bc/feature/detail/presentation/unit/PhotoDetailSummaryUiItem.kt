package com.bc.feature.detail.presentation.unit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bc.core.presentation.ui.ListSpan
import com.bc.core.presentation.ui.UiItem
import com.bc.feature.R
import com.bc.feature.detail.presentation.unit.mapper.toSummaryUiItem
import com.bc.feature.detail.presentation.unit.preview.PhotoDetailPreviewData
import com.bc.feature.detail.presentation.vm.intent.PhotoDetailIntent
import com.ssg.env.ds.composite.LocalText
import com.ssg.env.ds.foundation.SpaceToken
import com.ssg.env.ds.foundation.spacedBy

data class PhotoDetailSummaryUiItem(
    private val description: String?,
    private val altDescription: String?,
) : UiItem<PhotoDetailIntent>(ListSpan.FULL_FOR_ALL) {

    @Composable
    override fun SetItem(processIntent: (PhotoDetailIntent) -> Unit) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(SpaceToken.XXS)
        ) {
            if (!description.isNullOrEmpty()) {
                LocalText(
                    text = description,
                    color = colorResource(R.color.gray900),
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (!altDescription.isNullOrEmpty()) {
                LocalText(
                    text = altDescription,
                    color = colorResource(R.color.gray800),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
private fun Preview() {
    PhotoDetailPreviewData.photo.toSummaryUiItem()?.BuildItem {}
}
