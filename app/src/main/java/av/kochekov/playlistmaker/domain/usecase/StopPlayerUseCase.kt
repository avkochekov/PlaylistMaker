package av.kochekov.playlistmaker.domain.usecase

import av.kochekov.playlistmaker.domain.mediaplayer.api.MediaPlayerInterface

class StopPlayerUseCase(private val player: MediaPlayerInterface) {
    fun execute() {
        player.stopTrack()
    }
}