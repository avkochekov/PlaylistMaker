package av.kochekov.playlistmaker.search.presentation.composeUI

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.common.presentation.CustomText

@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    image: Int,
    text: String
) {
    Column(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            painter = painterResource(id = image),
            contentDescription = text)
        CustomText(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = text,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun ErrorMessagePreview(){
    ErrorMessage(
        image = R.drawable.connection_error, 
        text = stringResource(id = R.string.search_error_connectionFailed)
    )
}