package av.kochekov.playlistmaker.common.presentation

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import av.kochekov.playlistmaker.R

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    onClicked: () -> Unit?
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = getColor(color = R.attr.buttonBackgroundColor),
        ),
        shape = CircleShape,
        onClick = { onClicked() }
    ) {
        CustomText(
            text = text,
            color = getColor(color = R.attr.buttonTextColor)
        )
    }
}