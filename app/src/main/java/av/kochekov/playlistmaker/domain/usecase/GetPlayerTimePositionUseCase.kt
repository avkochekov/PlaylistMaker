package av.kochekov.playlistmaker.domain.usecase

import av.kochekov.playlistmaker.domain.mediaplayer.api.MediaPlayerInterface

class GetPlayerTimePositionUseCase(private val player: MediaPlayerInterface) {
    fun execute() : Int{
        return player.position()
    }
}