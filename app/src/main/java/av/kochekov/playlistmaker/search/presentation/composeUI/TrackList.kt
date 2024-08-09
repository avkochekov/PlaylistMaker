package av.kochekov.playlistmaker.search.presentation.composeUI

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import av.kochekov.playlistmaker.common.presentation.TrackItem
import av.kochekov.playlistmaker.search.domain.model.TrackModel

@Composable
fun TrackList(
    modifier: Modifier = Modifier,
    list: List<TrackModel>,
    onClicked: (TrackModel) -> Unit?
) {
    LazyColumn(modifier = modifier) {
        items(list) {track->
            TrackItem(
                track = track,
                onClicked = onClicked
            )
        }
    }
}