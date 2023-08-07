package av.kochekov.playlistmaker.domain.mediaplayer.api

interface MediaPlayerStateListenerInterface {
    fun onStateChanged(state: Int)
}