package av.kochekov.playlistmaker.playlist_view.domain

import av.kochekov.playlistmaker.playlist_view.domain.models.PlaylistModel

interface SharingInteractor {
    fun sharePlaylist(data: PlaylistModel)
}