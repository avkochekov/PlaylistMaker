package av.kochekov.playlistmaker.library.data.utils

import av.kochekov.playlistmaker.common.data.models.Track
import av.kochekov.playlistmaker.common.domain.TrackModel

object Mapper {
    fun toModel(data: Track) : TrackModel{
        return TrackModel(
            trackId = data.trackId,
            trackName = data.trackName,
            artistName = data.artistName,
            trackTimeMillis = data.trackTimeMillis,
            previewUrl = null,
            artworkUrl100 = data.artworkUrl100,
            collectionName = "",
            releaseDate = null,
            primaryGenreName = "",
            country = ""
        )
    }
}