package av.kochekov.playlistmaker.common.data.converters

import av.kochekov.playlistmaker.common.data.db.entity.PlaylistEntity
import av.kochekov.playlistmaker.common.data.db.relationships.PlaylistTracksRelation
import av.kochekov.playlistmaker.common.data.models.Playlist
import av.kochekov.playlistmaker.common.data.models.Track
import av.kochekov.playlistmaker.favorite.data.converters.TrackDbConvertor

class PlaylistDbConvertor {
    fun map(data: Playlist): PlaylistEntity {
        return PlaylistEntity(
            udi = data.uuid,
            name = data.name,
            description = data.description,
            image = data.artwork
        )
    }

    fun map(data: PlaylistEntity): Playlist {
        return Playlist(
            uuid = data.udi,
            name = data.name,
            artwork = data.image,
            description = data.description
        )
    }

    fun map(data: PlaylistTracksRelation): Playlist {
        val converter = TrackDbConvertor()
        return Playlist(
            uuid = data.playlist.udi,
            name = data.playlist.name,
            artwork = data.playlist.image,
            description = data.playlist.description,
            tracks = data.tracks.map { item -> converter.map(item) } as MutableList<Track>
        )
    }
}