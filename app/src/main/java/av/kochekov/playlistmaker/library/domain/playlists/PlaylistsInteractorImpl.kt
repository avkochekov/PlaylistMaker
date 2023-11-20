package av.kochekov.playlistmaker.library.domain.playlists

import av.kochekov.playlistmaker.library.domain.playlists.models.PlaylistModel
import av.kochekov.playlistmaker.common.domain.PlaylistRepository
import av.kochekov.playlistmaker.common.domain.PlaylistRepositoryObserver
import av.kochekov.playlistmaker.library.domain.playlists.utils.Converter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsInteractorImpl(private val playlistRepository: PlaylistRepository) : PlaylistsInteractor {
    override fun getPlaylists(): Flow<List<PlaylistModel>> = flow {
        playlistRepository.getPlaylists().collect { list ->
            val converter = Converter()
            emit(list.map { data -> converter.toModel(data) })
        }
    }

    override fun observe(observer: PlaylistRepositoryObserver) {
        playlistRepository.attach(observer)
    }
}