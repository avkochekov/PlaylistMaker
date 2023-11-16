package av.kochekov.playlistmaker.library.presentation.playlists

import av.kochekov.playlistmaker.common.data.models.Playlist

sealed interface  PlayListState {

    object Loading : PlayListState

    object Empty : PlayListState

    data class Content(
        val list: List<Playlist>
    ) : PlayListState

}