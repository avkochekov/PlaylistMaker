package av.kochekov.playlistmaker.player.domain.models
import av.kochekov.playlistmaker.common.data.models.Playlist

sealed class PlaylistListState {
    object Empty : PlaylistListState()
    data class Data(
        val list: List<Playlist>
    ) : PlaylistListState()
}