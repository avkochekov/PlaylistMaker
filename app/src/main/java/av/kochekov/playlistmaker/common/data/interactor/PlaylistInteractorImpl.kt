package av.kochekov.playlistmaker.common.data.interactor

import av.kochekov.playlistmaker.playlist.domain.PlaylistInteractor
import av.kochekov.playlistmaker.playlist.domain.PlaylistRepository
import av.kochekov.playlistmaker.playlist.domain.PlaylistRepositoryObserver
import av.kochekov.playlistmaker.common.data.models.Playlist
import av.kochekov.playlistmaker.common.data.models.Track
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository
) : PlaylistInteractor {
    override fun savePlaylist(data: Playlist) {
        GlobalScope.async {
            if (repository.containsPlaylists(data.udi).single()){
                repository.updatePlaylist(data)
            } else {
                repository.addPlaylist(data)
            }
        }
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override fun addToPlaylist(udi: String, track: Track) {
        GlobalScope.async {
            repository.addToPlaylist(udi, track)
        }
    }

    override fun contains(udi: String, id: Int): Flow<Boolean> {
        return repository.contains(udi, id)
    }

    override fun observe(observer: PlaylistRepositoryObserver) {
        repository.attach(observer)
    }
}