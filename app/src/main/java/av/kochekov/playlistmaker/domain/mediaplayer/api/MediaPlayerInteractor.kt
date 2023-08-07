package av.kochekov.playlistmaker.domain.mediaplayer.api

interface MediaPlayerInteractor {
    fun setTrack(track: String)
    fun play()
    fun pause()
    fun stop()
    fun timePosition() : Int
    fun duration() : Int

    fun setListener(listener: MediaPlayerStateListenerInterface)
}