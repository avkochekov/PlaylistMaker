package av.kochekov.playlistmaker.search.presentation.composeUI

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.common.presentation.CustomText
import av.kochekov.playlistmaker.common.presentation.getColor

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    text: String = String(),
    placeholder: String = String(),
    onTextChanged: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    Box (modifier = modifier
        .border(
            width = 0.dp,
            color = Color.Unspecified,
        )
        .clip(shape = RoundedCornerShape(8.dp))
        .background(color = getColor(R.attr.searchFieldColor))
        .fillMaxWidth()
        .height(36.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_16x16_search),
                contentDescription = null,
                tint = getColor(R.attr.searchHintColor)
                )
            Box (
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                if (text.isEmpty()) {
                    CustomText(
                        modifier = Modifier
                            .align(Alignment.CenterStart),
                        color = getColor(R.attr.searchHintColor),
                        text = placeholder,
                    )
                }
                BasicTextField(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = text,
                    singleLine = true,
                    onValueChange = {
                        onTextChanged(it)
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )
            }
            if (text.isNotEmpty()) {
                Icon(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .align(Alignment.CenterVertically)
                        .clickable { onTextChanged("") },
                    painter = painterResource(id = R.drawable.ic_16x16_clear),
                    contentDescription = null,
                    tint = getColor(R.attr.searchHintColor)
                )
            }
        }
    }
}