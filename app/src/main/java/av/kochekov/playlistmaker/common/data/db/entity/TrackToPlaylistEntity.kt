package av.kochekov.playlistmaker.common.data.db.entity

import androidx.room.Entity

@Entity(
    tableName = "track_to_playlist",
    primaryKeys = ["udi", "id"]
)
data class TrackToPlaylistEntity(
    val udi: String,
    val id: Int
)