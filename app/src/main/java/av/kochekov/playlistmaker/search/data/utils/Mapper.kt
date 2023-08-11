package av.kochekov.playlistmaker.search.data.utils

import av.kochekov.playlistmaker.search.data.model.Track
import av.kochekov.playlistmaker.search.domain.model.TrackInfo

object Mapper {
    fun toTrackInfo(track: Track) : TrackInfo {
        return TrackInfo(
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

    fun fromTrackInfo(track: TrackInfo) : Track {
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