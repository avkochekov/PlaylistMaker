package av.kochekov.playlistmaker.search.data.model

class TrackListResponse(
    val searchType: String,
    val expression: String,
    val results: List<Track>
) : Response()