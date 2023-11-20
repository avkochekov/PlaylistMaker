package av.kochekov.playlistmaker.playlist_view.domain

import av.kochekov.playlistmaker.common.domain.PlaylistRepository
import av.kochekov.playlistmaker.common.domain.PlaylistRepositoryObserver
import av.kochekov.playlistmaker.favorite.domain.TrackRepository
import av.kochekov.playlistmaker.playlist_view.domain.models.PlaylistModel
import av.kochekov.playlistmaker.playlist_view.domain.utils.Converter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class PlaylistViewInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val trackRepository: TrackRepository
) : PlaylistViewInteractor {
    override fun getPlaylist(uuid: String): Flow<PlaylistModel?> = flow {
        playlistRepository.getPlaylist(uuid).collect { data ->
            emit(data?.let { Converter().toModel(data) }?:null)
        }
    }

    override fun removeTrack(uuid: String, id: Int) {
        GlobalScope.async {
            playlistRepository.removeTrack(uuid, id)
        }
    }

    override fun removePlaylist(uuid: String) {
        GlobalScope.async {
            playlistRepository.removePlaylist(uuid)
        }
    }

    override fun observe(observer: PlaylistRepositoryObserver) {
        playlistRepository.attach(observer)
    }
}