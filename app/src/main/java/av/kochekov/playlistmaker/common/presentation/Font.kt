package av.kochekov.playlistmaker.common.presentation

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import av.kochekov.playlistmaker.R

val appFontFamily = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.ys_display_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.ys_display_medium,
            weight = FontWeight.Medium,
            style = FontStyle.Normal
        )
    )
)