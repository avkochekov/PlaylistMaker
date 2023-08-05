package av.kochekov.playlistmaker

import av.kochekov.playlistmaker.data.mediaplayer.MediaPlayerImpl
import av.kochekov.playlistmaker.domain.mediaplayer.api.MediaPlayerInteractor
import av.kochekov.playlistmaker.domain.mediaplayer.api.MediaPlayerInterface
import av.kochekov.playlistmaker.domain.mediaplayer.impl.MediaPlayerInteractorImpl

object MediaPlayerCreator {
    private fun getMediaPlayer(): MediaPlayerInterface {
        return MediaPlayerImpl()
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayer())
    }
}