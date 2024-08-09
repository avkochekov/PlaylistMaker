package av.kochekov.playlistmaker.common.presentation

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.settings.presentation.composeUI.CommonText

@Composable
fun CustomSwitch (
    modifier: Modifier = Modifier,
    title: String,
    checked: Boolean,
    onCheckedChanged: ((Boolean) -> Unit)?
) {
    Box(
        modifier = modifier.clickable {
            if (onCheckedChanged != null) {
                onCheckedChanged(!checked)
            }
        }
    ) {
        Row(
            modifier = Modifier
                .height(61.dp)
                .padding(
                    horizontal = 16.dp,
                )
        ){
            CommonText(
                modifier = Modifier
                    .weight(1f)
                    .align(alignment = Alignment.CenterVertically),
                text = title
            )
            Switch(
                modifier = Modifier.align(alignment = Alignment.CenterVertically),
                checked = checked,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(R.color.YP_blue),
                    checkedTrackColor = colorResource(R.color.YP_blue_light),
                    uncheckedThumbColor = colorResource(R.color.YP_gray),
                    uncheckedTrackColor = colorResource(R.color.YP_light_gray),
                ),
                onCheckedChange = {
                    if (onCheckedChanged != null) {
                        onCheckedChanged(checked)
                    }
                }
            )
        }
    }
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
fun CustomSwitchPreview () {
    CustomSwitch(title = "Title", checked = true, onCheckedChanged = {})
}