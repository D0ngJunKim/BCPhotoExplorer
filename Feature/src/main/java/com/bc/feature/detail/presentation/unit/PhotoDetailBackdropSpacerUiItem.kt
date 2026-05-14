package com.bc.feature.detail.presentation.unit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bc.core.domain.model.PhotoItemModel
import com.bc.core.presentation.ui.ListSpan
import com.bc.core.presentation.ui.UiItem
import com.bc.env.nav.LocalGlobalNavigator
import com.bc.feature.detail.presentation.unit.preview.PhotoDetailPreviewData
import com.bc.feature.detail.presentation.vm.intent.PhotoDetailIntent
import com.bc.feature.viewer.presentation.PhotoViewerRoute

internal data class PhotoDetailBackdropSpacerUiItem(
    private val photo: PhotoItemModel?,
    private val height: Dp
) : UiItem<PhotoDetailIntent>(ListSpan.FULL_FOR_ALL) {

    @Composable
    override fun SetItem(processIntent: (PhotoDetailIntent) -> Unit) {
        val navigator = LocalGlobalNavigator.current

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (photo != null) {
                        navigator?.navigate(PhotoViewerRoute(photo))
                    }
                }
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFEFEFEF)
private fun Preview() {
    PhotoDetailBackdropSpacerUiItem(PhotoDetailPreviewData.photo, height = 160.dp).BuildItem {}
}
