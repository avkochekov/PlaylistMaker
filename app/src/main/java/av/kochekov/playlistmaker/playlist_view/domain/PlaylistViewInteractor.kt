package av.kochekov.playlistmaker.playlist_view.domain

import av.kochekov.playlistmaker.common.domain.PlaylistRepository
import av.kochekov.playlistmaker.common.domain.PlaylistRepositoryObserver
import av.kochekov.playlistmaker.playlist_view.domain.models.PlaylistModel
import av.kochekov.playlistmaker.playlist_view.presentation.PlaylistViewViewModel
import kotlinx.coroutines.flow.Flow

interface PlaylistViewInteractor {
    fun getPlaylist(uuid: String) : Flow<PlaylistModel?>

    fun removeTrack(uuid: String, id: Int)

    fun removePlaylist(uuid: String)

    fun observe(observer: PlaylistRepositoryObserver)
}