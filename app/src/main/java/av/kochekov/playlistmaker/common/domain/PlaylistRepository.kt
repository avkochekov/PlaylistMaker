package av.kochekov.playlistmaker.common.domain

import av.kochekov.playlistmaker.common.data.models.Playlist
import av.kochekov.playlistmaker.common.data.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    fun addPlaylist(playlist: Playlist) : Flow<Playlist?>

    fun updatePlaylist(playlist: Playlist) : Flow<Playlist?>

    suspend fun removePlaylist(playlist: Playlist)

    suspend fun removePlaylist(uuid: String)

    fun getPlaylists(): Flow<List<Playlist>>

    fun getPlaylist(udi: String): Flow<Playlist?>

    fun containsPlaylists(udi: String): Flow<Boolean>

    fun containsTrack(udi: String, id: Int): Flow<Boolean>

    fun containsTrack(id: Int): Flow<Boolean>

    suspend fun addTrack(udi: String, track: Track)

    suspend fun removeTrack(udi: String, id: Int)

}