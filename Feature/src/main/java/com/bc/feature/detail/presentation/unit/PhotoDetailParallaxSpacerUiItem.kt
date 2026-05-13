package com.bc.feature.detail.presentation.unit

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bc.core.presentation.ui.ListSpan
import com.bc.core.presentation.ui.UiItem
import com.bc.feature.detail.presentation.vm.intent.PhotoDetailIntent

internal data class PhotoDetailParallaxSpacerUiItem(
    private val height: Dp
) : UiItem<PhotoDetailIntent>(ListSpan.FULL_FOR_ALL) {

    @Composable
    override fun SetItem(processIntent: (PhotoDetailIntent) -> Unit) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFEFEFEF)
private fun Preview() {
    PhotoDetailParallaxSpacerUiItem(height = 160.dp).BuildItem {}
}
