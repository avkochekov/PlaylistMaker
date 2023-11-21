package av.kochekov.playlistmaker.playlist_view.domain.models

import java.text.SimpleDateFormat
import java.util.*

data class TrackModel (
    val trackId: Int,               // Уникальный ID композиции
    val trackName: String,          // Название композиции
    val artistName: String,         // Имя исполнителя
    val artwork: String,
    val trackTimeMillis: Int?,      // Продолжительность трека в миллисекундах
) {
    val artwork60: String
        get() =  artwork.replaceAfterLast('/', "60x60bb.jpg")

    val duration: String
        get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
}
