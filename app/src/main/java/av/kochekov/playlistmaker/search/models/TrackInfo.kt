package av.kochekov.playlistmaker.search.models

import java.text.SimpleDateFormat
import java.util.*

data class TrackInfo(
    val trackId: Int,               // Уникальный ID композиции
    val trackName: String,          // Название композиции
    val artistName: String,         // Имя исполнителя
    val trackTimeMillis: Int?,      // Продолжительность трека в миллисекундах
    val artworkUrl100: String,      // Ссылка на изображение обложки
    val collectionName: String,     // Название альбома
    val releaseDate: String?,       // Год релиза TODO: проверить тип поля
    val primaryGenreName: String,   // Жанр трека
    val country: String,            // Страна исполнителя
    val previewUrl: String?         // Превью трека
) : java.io.Serializable {
    val artworkUrl512: String
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    val duration: String
        get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

    val releaseYear: String
        get() = releaseDate?.let {
            return@let SimpleDateFormat("yyyy", Locale.getDefault())
                .format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                    .parse(releaseDate))
        }?: ""
}
