package av.kochekov.playlistmaker.common.data.db.relationships

import androidx.room.Relation
import av.kochekov.playlistmaker.common.data.db.entity.TrackEntity

data class FavoriteTrackRelation(
    val id: Int,
    @Relation(parentColumn = "id", entity = TrackEntity::class, entityColumn = "id")
    val track: TrackEntity
)
