package av.kochekov.playlistmaker.playlist.data.utils

import av.kochekov.playlistmaker.common.data.models.Playlist
import av.kochekov.playlistmaker.playlist.domain.models.PlaylistModel

object Mapper {
    fun toModel(data: Playlist): PlaylistModel {
        return PlaylistModel(
            uuid = data.uuid,
            artwork = data.artwork,
            name = data.name,
            description = data.description,
            tracksCount = data.tracks.size
        )
    }

    fun fromModel(data: PlaylistModel): Playlist {
        return Playlist(
            uuid = data.uuid,
            artwork = data.artwork,
            name = data.name,
            description = data.description
        )
    }
}