package av.kochekov.playlistmaker.common.presentation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import av.kochekov.playlistmaker.R

@Composable
fun CustomText (
    text: String,
    textAlign: TextAlign? = null,
    fontSize: TextUnit = 16.sp,
    fontWeight:FontWeight = FontWeight.W400,
    color: Color = getColor(R.attr.textColor),
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontFamily = appFontFamily,
        color = color
    )
}