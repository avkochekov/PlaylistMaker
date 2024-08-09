package av.kochekov.playlistmaker.search.presentation.composeUI

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.common.presentation.CustomButton
import av.kochekov.playlistmaker.common.presentation.CustomText
import av.kochekov.playlistmaker.common.presentation.CustomTopAppBar
import av.kochekov.playlistmaker.common.presentation.getColor
import av.kochekov.playlistmaker.search.domain.model.ErrorMessageType
import av.kochekov.playlistmaker.search.domain.model.SearchFragmentState
import av.kochekov.playlistmaker.search.domain.model.TrackModel
import av.kochekov.playlistmaker.search.presentation.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchPage(
    onTrackClicked: (TrackModel) -> Unit?
) {
    var viewModel: SearchViewModel = koinViewModel()
    val state by viewModel.fragmentState().observeAsState()
    var text = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.menu_search),
            )
        },
        backgroundColor = getColor(R.attr.backgroundColor)
    ) {innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
        )
        {
            BasicTextField(
                value = String(),
                onValueChange = {}
            )
            SearchField(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
                text = text.value,
                placeholder = stringResource(R.string.menu_search),
                onTextChanged = {
                    text.value = it
                    viewModel.search(it)
                },
            )
            when (state) {
                is SearchFragmentState.HistoryList -> {
                    val list = (state as SearchFragmentState.HistoryList).trackList
                    val isEmpty = list.isEmpty()
                    if (!isEmpty) {
                        Box(
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .height(52.dp)
                                .fillMaxWidth()
                        ) {
                            CustomText(
                                modifier = Modifier.align(Alignment.Center),
                                text = stringResource(R.string.search_history_title),
                                textAlign = TextAlign.Center,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.W500
                            )
                        }
                    }
                    TrackList(
                        list = list,
                        onClicked = {track ->
                            viewModel.addToHistory(track)
                            onTrackClicked(track)
                        }
                    )
                    if (!isEmpty) {
                        CustomButton(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 24.dp),
                            text = stringResource(R.string.search_history_clear),
                            onClicked = {
                                viewModel.clearHistory()
                            }
                        )
                    }
                }
                is SearchFragmentState.SearchResultList -> {
                    TrackList(
                        list = (state as SearchFragmentState.SearchResultList).trackList,
                        onClicked = {track ->
                            viewModel.addToHistory(track)
                            onTrackClicked(track)
                        }
                    )
                }
                is SearchFragmentState.InSearchActivity -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 140.dp),
                        color = colorResource(R.color.blue)
                    )
                }
                is SearchFragmentState.Error -> {
                    val errorImage: Int
                    val errorText: String
                    val displayButton: Boolean
                    when ((state as SearchFragmentState.Error).error) {
                        ErrorMessageType.NO_CONNECTION -> {
                            errorImage = R.drawable.connection_error
                            errorText = stringResource(R.string.search_error_connectionFailed)
                            displayButton = true
                        }
                        ErrorMessageType.NO_DATA -> {
                            errorImage = R.drawable.search_error
                            errorText = stringResource(R.string.search_error_emptyTrackList)
                            displayButton = false
                        }
                    }
                    ErrorMessage(
                        modifier = Modifier
                            .padding(top = 102.dp)
                            .align(Alignment.CenterHorizontally),
                        image = errorImage,
                        text = errorText
                    )
                    if (displayButton) {
                        CustomButton(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = stringResource(id = R.string.search_update),
                            onClicked = {
                                viewModel.search()
                            }
                        )
                    }
                }
                else -> { }
            }

        }

    }
}

@Preview(
    name = "Light Mode",
    showSystemUi = true)
@Preview(
    name = "Dark Mode",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchPagePreview() {
    SearchPage(
        onTrackClicked = {}
    )
}
