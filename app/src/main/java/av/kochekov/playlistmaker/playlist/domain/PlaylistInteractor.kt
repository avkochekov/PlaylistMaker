package av.kochekov.playlistmaker.playlist.domain

import av.kochekov.playlistmaker.common.data.models.Playlist
import av.kochekov.playlistmaker.common.data.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun savePlaylist(data: Playlist)

    fun getPlaylists() : Flow<List<Playlist>>

    fun addToPlaylist(udi: String, track: Track)

    fun contains(udi: String, id: Int): Flow<Boolean>

    fun observe(observer: PlaylistRepositoryObserver)
}