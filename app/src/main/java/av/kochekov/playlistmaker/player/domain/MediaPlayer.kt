package av.kochekov.playlistmaker.player.domain

interface MediaPlayer {

    fun setTrack(trackPath: String)

    fun startTrack()

    fun pauseTrack()

    fun stopTrack()

    fun duration(): Int

    fun position(): Int

    fun state(): Int

    fun addListener(listener: MediaPlayerStateListenerInterface)
}