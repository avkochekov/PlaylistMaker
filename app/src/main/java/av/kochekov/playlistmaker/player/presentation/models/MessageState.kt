package av.kochekov.playlistmaker.player.presentation.models

sealed class MessageState {
    object Empty : MessageState()
    data class TrackAlreadyInPlaylist (
        val playlist: String
    ) : MessageState()

    data class AddTrackToPlaylistSuccess (
        val playlist: String
    ) : MessageState()
}
