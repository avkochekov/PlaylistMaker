package av.kochekov.playlistmaker.playlist.domain

import av.kochekov.playlistmaker.common.data.models.Playlist
import av.kochekov.playlistmaker.common.data.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepositoryObserver{
    fun update()
}

interface PlaylistRepository {

    companion object{
        var observerList: MutableList<PlaylistRepositoryObserver> = mutableListOf()
    }
    suspend fun addPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun removePlaylist(playlist: Playlist)

    fun getPlaylists() : Flow<List<Playlist>>

    fun getPlaylist(udi: String) : Flow<Playlist?>

    fun containsPlaylists(udi: String) : Flow<Boolean>

    fun contains(udi: String, id: Int): Flow<Boolean>

    suspend fun addToPlaylist(udi: String, track: Track)

    fun attach(observer: PlaylistRepositoryObserver){
        observerList.add(observer)
    }

    fun update(){
        observerList.map {
            it.update()
        }
    }
}