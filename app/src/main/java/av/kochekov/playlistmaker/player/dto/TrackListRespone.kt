package av.kochekov.playlistmaker.player.dto

class TrackListResponse(val searchType: String,
                        val expression: String,
                        val results: List<TrackDto>) : Response()