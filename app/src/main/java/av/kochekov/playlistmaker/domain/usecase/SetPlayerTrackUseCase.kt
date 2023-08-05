package av.kochekov.playlistmaker.domain.usecase

import av.kochekov.playlistmaker.domain.mediaplayer.api.MediaPlayerInterface

class SetPlayerTrackUseCase(private val player: MediaPlayerInterface) {
    fun execute(track: String){
        player.setTrack(track)
    }
}