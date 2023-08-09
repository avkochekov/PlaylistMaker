package av.kochekov.playlistmaker.player.domain

import av.kochekov.playlistmaker.player.models.MediaPlayerState

interface MediaPlayerStateListenerInterface {
    fun onStateChanged(state: MediaPlayerState)
}