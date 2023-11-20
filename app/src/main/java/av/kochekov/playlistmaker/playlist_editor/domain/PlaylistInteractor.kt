package av.kochekov.playlistmaker.playlist_editor.domain

import av.kochekov.playlistmaker.common.domain.PlaylistRepositoryObserver
import av.kochekov.playlistmaker.playlist_editor.domain.models.PlaylistModel
import av.kochekov.playlistmaker.search.domain.model.TrackModel
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun savePlaylist(data: PlaylistModel)

    fun getPlaylist(uuid: String): Flow<PlaylistModel?>

    fun getPlaylists(): Flow<List<PlaylistModel>>

    fun addToPlaylist(udi: String, track: TrackModel)

    fun contains(udi: String, id: Int): Flow<Boolean>

    fun observe(observer: PlaylistRepositoryObserver)
}