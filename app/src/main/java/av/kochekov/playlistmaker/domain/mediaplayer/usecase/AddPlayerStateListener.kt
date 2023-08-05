package av.kochekov.playlistmaker.domain.mediaplayer.usecase

import av.kochekov.playlistmaker.domain.mediaplayer.api.MediaPlayerInterface
import av.kochekov.playlistmaker.domain.mediaplayer.api.MediaPlayerStateListenerInterface

class AddPlayerStateListener(private val player: MediaPlayerInterface) {
    fun execute(listener: MediaPlayerStateListenerInterface){
        player.addListener(listener)
    }
}