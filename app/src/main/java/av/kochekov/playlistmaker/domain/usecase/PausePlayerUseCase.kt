package av.kochekov.playlistmaker.domain.usecase

import av.kochekov.playlistmaker.domain.mediaplayer.api.MediaPlayerInterface

class PausePlayerUseCase(private val player: MediaPlayerInterface) {
    fun execute(){
        player.pauseTrack()
    }
}