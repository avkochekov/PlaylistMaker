package av.kochekov.playlistmaker.player.domain

interface MediaPlayerInteractor {
    fun setTrack(track: String)
    fun play()
    fun pause()
    fun stop()
    fun timePosition(): Int
    fun duration(): Int

    fun setListener(listener: MediaPlayerStateListenerInterface)
}