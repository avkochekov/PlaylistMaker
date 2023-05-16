package av.kochekov.playlistmaker

data class TrackResponse (
    val resultCount: Int,
    val results: List<Track>
)