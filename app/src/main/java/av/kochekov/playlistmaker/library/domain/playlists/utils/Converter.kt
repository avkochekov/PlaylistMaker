package av.kochekov.playlistmaker.library.domain.playlists.utils

import av.kochekov.playlistmaker.common.data.models.Playlist
import av.kochekov.playlistmaker.library.domain.playlists.models.PlaylistModel

class Converter {
    fun toModel(data: Playlist) : PlaylistModel{
        return PlaylistModel(
            uuid = data.uuid,
            artwork = data.artwork,
            name = data.name,
            tracksCount = data.tracks.size
        )
    }
}