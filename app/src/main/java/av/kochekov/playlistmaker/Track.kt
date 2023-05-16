package av.kochekov.playlistmaker

data class Track(
    val trackName: String,      // Название композиции
    val artistName: String,     // Имя исполнителя
    val trackTimeMillis: Int,         // Продолжительность трека в миллисекундах
    val artworkUrl100: String   // Ссылка на изображение обложки
)