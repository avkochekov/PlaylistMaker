package av.kochekov.playlistmaker.library.presentation.playlists.models

import av.kochekov.playlistmaker.library.domain.playlists.models.PlaylistModel

sealed interface PlaylistState {

    object Loading : PlaylistState

    object Empty : PlaylistState

    data class Content(
        val list: List<PlaylistModel>
    ) : PlaylistState

}