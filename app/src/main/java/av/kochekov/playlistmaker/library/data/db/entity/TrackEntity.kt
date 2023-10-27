package av.kochekov.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks")
data class TrackEntity(
    @PrimaryKey
    val id: Int,                // идентификатор трека
    val trackName: String,      // название трека
    val artistName: String,     // имя исполнителя
    val trackTime: Int?,        // продолжительность трека
    val coverLink: String,      // ссылка на обложку
    val albumName: String,      // название альбома
    val releaseDate: String?,   // год релиза трека
    val trackGenre: String,     // жанр трека
    val country: String,        // страна исполнителя
    val previewUrl: String?,   // ссылка на файл для воспроизведения
)
