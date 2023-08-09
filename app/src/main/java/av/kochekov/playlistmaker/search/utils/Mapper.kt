package av.kochekov.playlistmaker.search.utils

import av.kochekov.playlistmaker.repository.track_list_repository.models.Track
import av.kochekov.playlistmaker.search.models.TrackInfo

object Mapper {
    fun toTrackInfo(track: Track) : TrackInfo{
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

    fun fromTrackInfo(track: TrackInfo) : Track{
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