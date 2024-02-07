package av.kochekov.playlistmaker.common.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey
    val udi: String,
    val name: String,
    val description: String,
    val image: String,
)