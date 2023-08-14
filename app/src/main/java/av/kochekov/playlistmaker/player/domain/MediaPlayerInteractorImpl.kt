package av.kochekov.playlistmaker.player.domain

import av.kochekov.playlistmaker.player.domain.MediaPlayerInteractor
import av.kochekov.playlistmaker.player.domain.MediaPlayer
import av.kochekov.playlistmaker.player.domain.MediaPlayerStateListenerInterface

class MediaPlayerInteractorImpl(private val player: MediaPlayer) : MediaPlayerInteractor {
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
