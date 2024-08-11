package av.kochekov.playlistmaker.library.presentation.composeUI

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.common.presentation.CustomButton
import av.kochekov.playlistmaker.common.presentation.ErrorMessage
import av.kochekov.playlistmaker.common.presentation.PlaylistList
import av.kochekov.playlistmaker.library.domain.playlists.models.PlaylistModel
import av.kochekov.playlistmaker.library.presentation.playlists.PlaylistsViewModel
import av.kochekov.playlistmaker.library.presentation.playlists.models.PlaylistState
import org.koin.androidx.compose.koinViewModel

@Composable
fun LibraryPlaylistPage(
    modifier: Modifier = Modifier,
    onPlaylistClicked: (PlaylistModel) -> Unit?,
    onNewPlaylistClicked: () -> Unit?
) {
    val viewModel: PlaylistsViewModel = koinViewModel()
    val state by viewModel.state().observeAsState()

    LaunchedEffect(key1 = null) {
        viewModel.load()
    }

    Column(
        modifier = modifier.padding(
            top = 24.dp,
            bottom = 16.dp
        )
    ) {
        CustomButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.library_newPlayList),
            onClicked = onNewPlaylistClicked
        )
        when (state) {

            is PlaylistState.Content -> {
                PlaylistList(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    list = (state as PlaylistState.Content).list,
                    onItemClicked = onPlaylistClicked
                )
            }

            is PlaylistState.Empty -> {
                ErrorMessage(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 46.dp),
                    image = R.drawable.search_error,
                    text = stringResource(id = R.string.library_error_playLists)
                )
            }

            else -> {}
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
fun LibraryPlaylistPagePreview() {
    LibraryPlaylistPage(
        onPlaylistClicked = {},
        onNewPlaylistClicked = {}
    )
}