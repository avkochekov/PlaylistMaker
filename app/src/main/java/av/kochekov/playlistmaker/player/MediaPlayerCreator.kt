package av.kochekov.playlistmaker.player

import av.kochekov.playlistmaker.player.data.MediaPlayerImpl
import av.kochekov.playlistmaker.player.domain.MediaPlayerInteractor
import av.kochekov.playlistmaker.player.domain.MediaPlayer
import av.kochekov.playlistmaker.player.domain.MediaPlayerInteractorImpl

object MediaPlayerCreator {
    private fun getMediaPlayer(): MediaPlayer {
        return MediaPlayerImpl()
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayer())
    }
}