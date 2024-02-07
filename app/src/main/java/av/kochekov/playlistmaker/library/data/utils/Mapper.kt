package av.kochekov.playlistmaker.library.data.utils

import av.kochekov.playlistmaker.common.data.models.Track
import av.kochekov.playlistmaker.library.domain.favorite.models.TrackModel

object Mapper {
    fun toModel(data: Track) : TrackModel{
        return TrackModel(
            trackId = data.trackId,
            trackName = data.trackName,
            artistName = data.artistName,
            artwork = data.artworkUrl100,
            trackTimeMillis = data.trackTimeMillis
        )
    }
}