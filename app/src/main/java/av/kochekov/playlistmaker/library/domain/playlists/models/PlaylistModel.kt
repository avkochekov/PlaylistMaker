package av.kochekov.playlistmaker.library.domain.playlists.models

data class PlaylistModel (
    val uuid: String,
    val artwork: String,
    val name: String,
    val tracksCount: Int
)