package av.kochekov.playlistmaker.domain.mediaplayer.api

interface MediaPlayerInteractor {
    fun setTrack(track: String)
    fun play()
    fun pause()
    fun stop()
    fun timePosition() : Int
    fun duration() : Int

    fun isPlaying() : Boolean

    fun isPaused() : Boolean

    fun isStopped() : Boolean

    fun isReady() : Boolean

    fun setListener(listener: MediaPlayerStateListenerInterface)
}