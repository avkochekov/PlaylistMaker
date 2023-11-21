package av.kochekov.playlistmaker.library.domain.playlists

import av.kochekov.playlistmaker.library.domain.playlists.models.PlaylistModel
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun getPlaylists() : Flow<List<PlaylistModel>>
}