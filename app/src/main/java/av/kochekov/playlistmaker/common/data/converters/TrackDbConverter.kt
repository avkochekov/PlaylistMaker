package av.kochekov.playlistmaker.favorite.data.converters

import av.kochekov.playlistmaker.common.data.db.entity.FavoriteTrackEntity
import av.kochekov.playlistmaker.common.data.db.entity.TrackEntity
import av.kochekov.playlistmaker.common.data.db.relationships.FavoriteTrackRelation
import av.kochekov.playlistmaker.common.data.models.Track

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

    fun map(rel: FavoriteTrackRelation): Track {
        return Track(
            rel.track.id,
            rel.track.trackName,
            rel.track.artistName,
            rel.track.trackTime,
            rel.track.coverLink,
            rel.track.albumName,
            rel.track.releaseDate,
            rel.track.trackGenre,
            rel.track.country,
            rel.track.previewUrl
        )
    }

    fun map(track: FavoriteTrackEntity): Int {
        return track.id
    }

    fun map(track: Int): FavoriteTrackEntity {
        return FavoriteTrackEntity(
            track
        )
    }
}