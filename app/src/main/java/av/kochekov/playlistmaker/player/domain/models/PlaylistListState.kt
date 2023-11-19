package av.kochekov.playlistmaker.player.domain.models

import av.kochekov.playlistmaker.playlist.domain.models.PlaylistModel

sealed class PlaylistListState {
    object Empty : PlaylistListState()
    data class Data(
        val list: List<PlaylistModel>
    ) : PlaylistListState()
}