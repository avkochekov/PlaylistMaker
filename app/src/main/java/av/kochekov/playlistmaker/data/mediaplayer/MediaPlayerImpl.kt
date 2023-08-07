package av.kochekov.playlistmaker.data.mediaplayer

import android.media.MediaPlayer
import av.kochekov.playlistmaker.domain.mediaplayer.api.MediaPlayerInterface
import av.kochekov.playlistmaker.domain.mediaplayer.api.MediaPlayerStateListenerInterface
import av.kochekov.playlistmaker.domain.mediaplayer.model.MediaPlayerState

class MediaPlayerImpl : MediaPlayer(), MediaPlayerInterface {

    private var state = MediaPlayerState.STATE_DEFAULT
    private var listenerList: MutableList<MediaPlayerStateListenerInterface> = mutableListOf()

    override fun setTrack(track: String) {
        setDataSource(track)
        prepareAsync()
        setOnPreparedListener {
            setState(MediaPlayerState.STATE_PREPARED)
        }
        setOnCompletionListener {
            setState(MediaPlayerState.STATE_PREPARED)
        }
    }

    override fun startTrack() {
        start()
        setState(MediaPlayerState.STATE_PLAYING)
    }

    override fun pauseTrack() {
        pause()
        setState(MediaPlayerState.STATE_PAUSED)
    }

    override fun stopTrack() {
        stop()
        setState(MediaPlayerState.STATE_PREPARED)
    }

    override fun duration(): Int {
        return duration;
    }

    override fun position(): Int {
        return currentPosition
    }

    override fun state(): Int {
        return state
    }

    override fun addListener(listener: MediaPlayerStateListenerInterface) {
        listenerList.add(listener)
    }

    private fun setState(state: Int){
        if (this.state == state)
            return
        this.state = state
        listenerList.map {
            it.onStateChanged(this.state)
        }
    }
}