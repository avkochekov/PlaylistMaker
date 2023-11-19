package av.kochekov.playlistmaker.common.data.db.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import av.kochekov.playlistmaker.common.data.db.entity.PlaylistEntity
import av.kochekov.playlistmaker.common.data.db.entity.TrackEntity
import av.kochekov.playlistmaker.common.data.db.entity.TrackToPlaylistEntity

data class PlaylistTracksRelation(
    @Embedded
    val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "udi",
        entityColumn = "id",
        associateBy = Junction(TrackToPlaylistEntity::class)
    )
    val tracks: List<TrackEntity>
)
