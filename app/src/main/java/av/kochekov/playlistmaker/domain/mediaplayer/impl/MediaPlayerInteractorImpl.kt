package av.kochekov.playlistmaker.domain.mediaplayer.impl

import av.kochekov.playlistmaker.domain.mediaplayer.api.MediaPlayerInteractor
import av.kochekov.playlistmaker.domain.mediaplayer.api.MediaPlayerInterface
import av.kochekov.playlistmaker.domain.mediaplayer.api.MediaPlayerStateListenerInterface
import av.kochekov.playlistmaker.domain.mediaplayer.model.MediaPlayerState

class MediaPlayerInteractorImpl(private val player: MediaPlayerInterface) : MediaPlayerInteractor {
    override fun setTrack(track: String) {
        player.setTrack(track)
    }

    override fun play() {
        player.startTrack()
    }

    override fun pause() {
        player.pauseTrack()
    }

    override fun stop() {
        player.stopTrack()
    }

    override fun timePosition(): Int {
        return player.position()
    }

    override fun duration(): Int {
        return player.duration()

    }

    override fun setListener(listener: MediaPlayerStateListenerInterface) {
        player.addListener(listener)
    }
}
