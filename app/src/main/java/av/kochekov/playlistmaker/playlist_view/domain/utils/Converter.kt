package av.kochekov.playlistmaker.playlist_view.domain.utils

import av.kochekov.playlistmaker.common.data.models.Playlist
import av.kochekov.playlistmaker.common.data.models.Track
import av.kochekov.playlistmaker.playlist_view.domain.models.PlaylistModel
import av.kochekov.playlistmaker.playlist_view.domain.models.TrackModel

class Converter {
    fun toModel(data: Playlist) : PlaylistModel {
        return PlaylistModel(
            uuid = data.uuid,
            artwork = data.artwork,
            name = data.name,
            description = data.description,
            tracks = data.tracks.map { data -> toModel(data) }
        )
    }

    fun toModel(data: Track) : TrackModel {
        return TrackModel(
            trackId = data.trackId,
            trackName = data.trackName,
            artistName = data.artistName,
            trackTimeMillis = data.trackTimeMillis,
            artwork = data.artworkUrl100
        )
    }
}