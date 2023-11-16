package av.kochekov.playlistmaker.library.presentation.favorite

import av.kochekov.playlistmaker.search.domain.model.TrackModel

sealed interface FavoriteTrackListState {
    object Loading : FavoriteTrackListState

    object Empty : FavoriteTrackListState

    data class Content(
        val tracks: List<TrackModel>
    ) : FavoriteTrackListState
}


