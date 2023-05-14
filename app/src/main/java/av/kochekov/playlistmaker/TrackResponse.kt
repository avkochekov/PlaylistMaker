package av.kochekov.playlistmaker

class TrackResponseItem(
//    val artistName: String,
//    val artworkUrl100: String,
//    val trackName: String,
//    val trackTimeMillis: Int
)

class TrackResponse (
    val resultCount: Int,
    val results: List<TrackResponseItem>
)