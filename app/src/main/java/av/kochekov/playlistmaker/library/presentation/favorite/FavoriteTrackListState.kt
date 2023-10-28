package av.kochekov.playlistmaker.library.presentation.favorite

import av.kochekov.playlistmaker.search.data.model.Track
import av.kochekov.playlistmaker.search.domain.model.TrackInfo

sealed interface FavoriteTrackListState {
    object Loading : FavoriteTrackListState

    object Empty : FavoriteTrackListState

    data class Content(
        val tracks: List<TrackInfo>
    ) : FavoriteTrackListState
}


