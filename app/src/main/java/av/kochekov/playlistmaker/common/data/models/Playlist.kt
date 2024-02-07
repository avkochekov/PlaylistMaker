package av.kochekov.playlistmaker.common.data.models

data class Playlist(
    var uuid: String,
    var artwork: String,
    var name: String,
    var description: String,
    val tracks: MutableList<Track> = mutableListOf()
)
