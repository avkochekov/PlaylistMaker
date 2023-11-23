package av.kochekov.playlistmaker.playlist_view.domain

import av.kochekov.playlistmaker.playlist_view.domain.models.PlaylistModel
import kotlinx.coroutines.flow.Flow

interface PlaylistViewInteractor {
    fun getPlaylist(uuid: String) : Flow<PlaylistModel?>

    fun removeTrack(uuid: String, id: Int) : Flow<PlaylistModel>

    fun removePlaylist(uuid: String)
}