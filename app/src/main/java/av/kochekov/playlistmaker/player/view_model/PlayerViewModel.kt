package av.kochekov.playlistmaker.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import av.kochekov.playlistmaker.player.domain.MediaPlayerInteractor
import av.kochekov.playlistmaker.player.domain.MediaPlayerStateListenerInterface
import av.kochekov.playlistmaker.player.models.MediaPlayerState
import av.kochekov.playlistmaker.search.models.TrackInfo

private const val TIME_UPDATE_VALUE_MILLIS = 200L

class PlayerViewModel (
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {

    private var trackInfo = MutableLiveData<TrackInfo>()
    private var trackPosition = MutableLiveData<Int>()
    private var playerState = MutableLiveData<MediaPlayerState>()

    private val handler = Handler(Looper.getMainLooper())
    private val timeUpdateRunnable = Runnable { updateRemainingTime() }

    init {
        mediaPlayerInteractor.setListener(object : MediaPlayerStateListenerInterface {
            override fun onStateChanged(state: MediaPlayerState) {
                playerState.value = state
            }
        })
    }

    fun playerState() : LiveData<MediaPlayerState> {
        return playerState
    }

    fun trackPosition() : LiveData<Int> {
        return trackPosition
    }

    fun trackInfo() : LiveData<TrackInfo> {
        return trackInfo
    }

    fun setTrack(track: TrackInfo) {
        trackInfo.value = track
        trackPosition.value = 0
        mediaPlayerInteractor.setTrack(track.previewUrl.toString())
    }

    fun onPlayClicked(){
        when(playerState.value){
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
    }

    private fun updateRemainingTime(){
        when(playerState.value){
            MediaPlayerState.STATE_DEFAULT,
            MediaPlayerState.STATE_PREPARED -> {
                trackPosition.value = 0
                handler.removeCallbacks(timeUpdateRunnable)
            }
            MediaPlayerState.STATE_PLAYING -> {
                trackPosition.value = mediaPlayerInteractor.timePosition()
                handler.postDelayed(timeUpdateRunnable, TIME_UPDATE_VALUE_MILLIS)
            }
            MediaPlayerState.STATE_PAUSED -> {
                handler.removeCallbacks(timeUpdateRunnable)
            }
            else -> {}
        }
    }

    companion object {
        fun getPlayerModelFactory(
            mediaPlayerInteractor: MediaPlayerInteractor
        ) : ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(
                        mediaPlayerInteractor = mediaPlayerInteractor
                    ) as T
                }
            }
    }
}