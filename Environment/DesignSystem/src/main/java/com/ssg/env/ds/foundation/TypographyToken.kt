package com.ssg.env.ds.foundation

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.ssg.env.ds.R

object TypographyToken {
    private val Pretendard = FontFamily(
        Font(R.font.pretendard_regular, FontWeight.Normal),
        Font(R.font.pretendard_medium, FontWeight.Medium),
        Font(R.font.pretendard_semibold, FontWeight.SemiBold),
        Font(R.font.pretendard_bold, FontWeight.Bold)
    )

    val Default = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal
    )
}