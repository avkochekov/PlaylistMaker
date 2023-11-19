package av.kochekov.playlistmaker.search.data.utils

import av.kochekov.playlistmaker.common.data.models.Track
import av.kochekov.playlistmaker.search.domain.model.TrackModel

object Mapper {
    fun toModel(data: Track): TrackModel {
        return TrackModel(
            data.trackId,
            data.trackName,
            data.artistName,
            data.trackTimeMillis,
            data.artworkUrl100,
            data.collectionName,
            data.releaseDate,
            data.primaryGenreName,
            data.country,
            data.previewUrl
        )
    }

    fun fromModel(data: TrackModel): Track {
        return Track(
            data.trackId,
            data.trackName,
            data.artistName,
            data.trackTimeMillis,
            data.artworkUrl100,
            data.collectionName,
            data.releaseDate,
            data.primaryGenreName,
            data.country,
            data.previewUrl
        )
    }
}