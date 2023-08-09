package av.kochekov.playlistmaker.creator

import av.kochekov.playlistmaker.player.data.MediaPlayerImpl
import av.kochekov.playlistmaker.player.domain.MediaPlayerInteractor
import av.kochekov.playlistmaker.player.domain.MediaPlayer
import av.kochekov.playlistmaker.player.data.MediaPlayerInteractorImpl

object MediaPlayerCreator {
    private fun getMediaPlayer(): MediaPlayer {
        return MediaPlayerImpl()
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayer())
    }
}