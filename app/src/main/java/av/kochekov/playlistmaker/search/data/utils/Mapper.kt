package av.kochekov.playlistmaker.search.data.utils

import av.kochekov.playlistmaker.common.data.models.Track
import av.kochekov.playlistmaker.search.domain.model.TrackModel

object Mapper {
    fun toModel(track: Track): TrackModel {
        return TrackModel(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun fromModel(track: TrackModel): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}