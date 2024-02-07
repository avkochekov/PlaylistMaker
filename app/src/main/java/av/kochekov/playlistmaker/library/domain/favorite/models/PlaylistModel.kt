package av.kochekov.playlistmaker.library.domain.favorite.models

data class PlaylistModel(
    var uuid: String,
    var artwork: String,
    var name: String,
    var description: String,
    var tracks: List<TrackModel>
)
