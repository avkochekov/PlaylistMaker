package av.kochekov.playlistmaker.data.dto

class TrackListResponse(val searchType: String,
                        val expression: String,
                        val results: List<TrackDto>) : Response()