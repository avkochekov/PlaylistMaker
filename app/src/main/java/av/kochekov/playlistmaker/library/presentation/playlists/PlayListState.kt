package av.kochekov.playlistmaker.library.presentation.playlists

import av.kochekov.playlistmaker.playlist.domain.models.PlaylistModel

sealed interface PlayListState {

    object Loading : PlayListState

    object Empty : PlayListState

    data class Content(
        val list: List<PlaylistModel>
    ) : PlayListState

}