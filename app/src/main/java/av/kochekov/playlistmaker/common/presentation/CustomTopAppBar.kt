package av.kochekov.playlistmaker.common.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.settings.presentation.composeUI.SettingsPage

@Composable
fun TopBarText (
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontSize = 22.sp,
        fontFamily = appFontFamily,
    )
}

@Composable
fun CustomTopAppBar(
    title: String = String(),
) {
    TopAppBar(
        modifier = Modifier
            .height(56.dp)
            .shadow(0.dp),
        title = {
            TopBarText(
                text = title,
                color = getColor(R.attr.textColor)
            )
        },
        backgroundColor = getColor(R.attr.backgroundColor),
        elevation = 0.dp
    )
}

@Preview(
    showSystemUi = true,
    device = "spec:orientation=portrait,width=411dp,height=891dp",
    name = "Light mode"
)
@Preview(
    showSystemUi = true,
    device = "spec:orientation=portrait,width=411dp,height=891dp",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Night mode"
)
@Composable
fun CustomTopAppBarPreview() {
    CustomTopAppBar(title = "Title")
}