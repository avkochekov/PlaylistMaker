package av.kochekov.playlistmaker.settings.presentation.composeUI

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.common.presentation.CustomText
import av.kochekov.playlistmaker.common.presentation.getColor

@Composable
fun CustomSettingsItem (
    modifier: Modifier = Modifier,
    title: String,
    imageResource: Int?,
    onClicked: (() -> Unit)?
) {
    Box(
        modifier = modifier
            .clickable {
            if (onClicked != null) {
                onClicked()
            }
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(61.dp)
                .padding(
                    horizontal = 16.dp,
                    vertical = 21.dp
                )
        ) {
            CustomText(
                modifier = Modifier
                    .weight(1f)
                    .align(alignment = Alignment.CenterVertically),
                text = title
            )
            if (imageResource != null) {
                Icon(
                    modifier = Modifier
                        .align(alignment = Alignment.CenterVertically),
                    painter = painterResource(id = imageResource),
                    tint = getColor(R.attr.imageTintColor),
                    contentDescription = title
                )
            }
        }
    }
}