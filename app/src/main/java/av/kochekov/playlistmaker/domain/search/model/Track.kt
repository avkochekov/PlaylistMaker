package av.kochekov.playlistmaker.domain.search.model

data class Track(
    val trackId: Int,               // Уникальный ID композиции
    val trackName: String,          // Название композиции
    val artistName: String,         // Имя исполнителя
    val trackTimeMillis: Int,       // Продолжительность трека в миллисекундах
    val artworkUrl100: String,      // Ссылка на изображение обложки
    val collectionName: String,     // Название альбома
    val releaseDate: String,        // Год релиза TODO: проверить тип поля
    val primaryGenreName: String,   // Жанр трека
    val country: String,            // Страна исполнителя
    val previewUrl: String          // Превью трека
)