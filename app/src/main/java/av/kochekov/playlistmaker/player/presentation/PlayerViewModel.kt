package av.kochekov.playlistmaker.player.presentation

import androidx.lifecycle.*
import av.kochekov.playlistmaker.player.domain.MediaPlayerInteractor
import av.kochekov.playlistmaker.player.domain.MediaPlayerStateListenerInterface
import av.kochekov.playlistmaker.player.domain.models.MediaPlayerState
import av.kochekov.playlistmaker.search.domain.model.TrackInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TIME_UPDATE_VALUE_MILLIS = 300L
private const val DEFAULT_TRACK_POSITION = 0

class PlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {

    private var trackInfo = MutableLiveData<TrackInfo>()
    private var trackPosition = MutableLiveData<Int>()
    private var playerState = MutableLiveData<MediaPlayerState>()

    private var timerJob: Job? = null

    init {
        mediaPlayerInteractor.setListener(object : MediaPlayerStateListenerInterface {
            override fun onStateChanged(state: MediaPlayerState) {
                playerState.value = state
            }
        })
    }

    fun playerState(): LiveData<MediaPlayerState> {
        return playerState
    }

    fun trackPosition(): LiveData<Int> {
        return trackPosition
    }

    fun trackInfo(): LiveData<TrackInfo> {
        return trackInfo
    }

    fun setTrack(track: TrackInfo) {
        trackInfo.value = track
        trackPosition.value = DEFAULT_TRACK_POSITION
        mediaPlayerInteractor.setTrack(track.previewUrl.toString())
    }

    fun onPlayClicked() {
        when (playerState.value) {
            MediaPlayerState.STATE_PLAYING -> mediaPlayerInteractor.pause()
            MediaPlayerState.STATE_PAUSED,
            MediaPlayerState.STATE_PREPARED -> mediaPlayerInteractor.play()
            else -> {}
        }
        updateRemainingTime()
    }

    fun stopPlayer() {
        playerState.value?.let {
            if (it == MediaPlayerState.STATE_DEFAULT)
                return
            mediaPlayerInteractor.stop()
        }
        timerJob?.cancel()
    }

    fun pausePlayer() {
        playerState.value?.let {
            if (it == MediaPlayerState.STATE_DEFAULT)
                return
            mediaPlayerInteractor.pause()
        }
    }

    private fun updateRemainingTime() {
        timerJob = viewModelScope.launch {
            while (playerState?.value == MediaPlayerState.STATE_PLAYING || playerState?.value == MediaPlayerState.STATE_PAUSED) {
                delay(TIME_UPDATE_VALUE_MILLIS)
                trackPosition.postValue(mediaPlayerInteractor.timePosition())
            }
            trackPosition.postValue(0)
        }
    }
}