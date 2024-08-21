package av.kochekov.playlistmaker.library.presentation.composeUI

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.common.presentation.ErrorMessage
import av.kochekov.playlistmaker.common.presentation.TrackList
import av.kochekov.playlistmaker.library.presentation.favorite.FavoriteTrackListState
import av.kochekov.playlistmaker.library.presentation.favorite.FavoriteTracksViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LibraryFavoritePage(
    modifier: Modifier = Modifier,
    onTrackClicked: (Int) -> Unit?
) {
    val viewModel: FavoriteTracksViewModel = koinViewModel()
    val state by viewModel.activityState().observeAsState()

    LaunchedEffect(key1 = null) {
        viewModel.load()
    }

    Box (
        modifier = modifier
    ) {
        when (val data = state) {
            is FavoriteTrackListState.Empty -> {
                ErrorMessage(
                    modifier = Modifier
                        .padding(top = 106.dp)
                        .align(Alignment.TopCenter),
                    image = R.drawable.search_error,
                    text = stringResource(id = R.string.library_error_favoriteTracks)
                )
            }

            is FavoriteTrackListState.Content -> {
                TrackList(
                    modifier = Modifier.fillMaxSize(),
                    list = data.tracks,
                    onClicked = {track ->
                        onTrackClicked(track.trackId)
                    }
                )
            }

            else -> {}
        }
    }
}