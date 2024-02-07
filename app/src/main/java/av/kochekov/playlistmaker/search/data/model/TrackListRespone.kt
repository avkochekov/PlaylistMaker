package av.kochekov.playlistmaker.search.data.model

import av.kochekov.playlistmaker.common.data.models.Track

class TrackListResponse(
    val searchType: String,
    val expression: String,
    val results: List<Track>
) : Response()