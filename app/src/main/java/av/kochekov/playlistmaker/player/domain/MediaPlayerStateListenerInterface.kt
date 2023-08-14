package av.kochekov.playlistmaker.player.domain

import av.kochekov.playlistmaker.player.domain.models.MediaPlayerState

interface MediaPlayerStateListenerInterface {
    fun onStateChanged(state: MediaPlayerState)
}