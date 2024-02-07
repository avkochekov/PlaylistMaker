package av.kochekov.playlistmaker.playlist_editor.domain.models

data class PlaylistModel(
    var uuid: String,
    var artwork: String,
    var name: String,
    var description: String,
    var tracksCount: Int
)
