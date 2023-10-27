package av.kochekov.playlistmaker.library.data.converters

import av.kochekov.playlistmaker.library.data.db.entity.TrackEntity
import av.kochekov.playlistmaker.search.data.model.Track

class TrackDbConvertor {
    fun map(track: Track): TrackEntity {
        return TrackEntity(
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

    fun map(track: TrackEntity): Track {
        return Track(
            track.id,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.coverLink,
            track.albumName,
            track.releaseDate,
            track.trackGenre,
            track.country,
            track.previewUrl
        )
    }
}