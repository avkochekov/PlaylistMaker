package av.kochekov.playlistmaker.domain.mediaplayer.impl

import av.kochekov.playlistmaker.domain.mediaplayer.api.MediaPlayerInteractor
import av.kochekov.playlistmaker.domain.mediaplayer.api.MediaPlayerInterface

class MediaPlayerInteractorImpl(private val player: MediaPlayerInterface) : MediaPlayerInteractor {

    override fun player(): MediaPlayerInterface {
        return player
    }
}
