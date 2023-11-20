package av.kochekov.playlistmaker.player.presentation

import android.util.Log
import androidx.lifecycle.*
import av.kochekov.playlistmaker.favorite_tracks.domain.TrackInteractor
import av.kochekov.playlistmaker.player.domain.MediaPlayerInteractor
import av.kochekov.playlistmaker.player.domain.MediaPlayerStateListenerInterface
import av.kochekov.playlistmaker.player.domain.models.MediaPlayerState
import av.kochekov.playlistmaker.player.domain.models.PlaylistListState
import av.kochekov.playlistmaker.player.presentation.models.MessageState
import av.kochekov.playlistmaker.playlist_editor.domain.PlaylistInteractor
import av.kochekov.playlistmaker.common.domain.PlaylistRepositoryObserver
import av.kochekov.playlistmaker.playlist_editor.domain.models.PlaylistModel
import av.kochekov.playlistmaker.search.domain.model.TrackModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

private const val TIME_UPDATE_VALUE_MILLIS = 300L
private const val DEFAULT_TRACK_POSITION = 0

class PlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoriteTrackInteractor: TrackInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val trackInteractor: TrackInteractor
) : ViewModel(), PlaylistRepositoryObserver {

    private var trackModel = MutableLiveData<TrackModel>()
    private var trackPosition = MutableLiveData<Int>()
    private var playerState = MutableLiveData<MediaPlayerState>()
    private var trackInFavorite = MutableLiveData<Boolean>()

    private var message = MutableLiveData<MessageState>()

    private var playlistState = MutableLiveData<PlaylistListState>()

    private var timerJob: Job? = null

    init {
        playlistInteractor.observe(this)
        mediaPlayerInteractor.setListener(object : MediaPlayerStateListenerInterface {
            override fun onStateChanged(state: MediaPlayerState) {
                playerState.postValue(state)
            }
        })
        checkTrackInFavorite()
        loadPlaylists()
    }

    fun playerState(): LiveData<MediaPlayerState> {
        return playerState
    }

    fun trackPosition(): LiveData<Int> {
        return trackPosition
    }

    fun trackInfo(): LiveData<TrackModel> {
        return trackModel
    }

    fun trackInFavorite(): LiveData<Boolean> {
        return trackInFavorite
    }

    fun playlistState(): LiveData<PlaylistListState> {
        return playlistState
    }

    fun message(): LiveData<MessageState> {
        return message
    }

    fun setTrack(track: TrackModel) {
        trackModel.postValue(track)
        trackPosition.postValue(DEFAULT_TRACK_POSITION)
        mediaPlayerInteractor.setTrack(track.previewUrl.toString())
        checkTrackInFavorite(track.trackId)
    }

    fun setTrack(track: Int) {
        GlobalScope.async {
            trackInteractor.getTrack(track).collect { data ->
                data?.let { setTrack(it) }
            }
        }
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
        playerState.value?.let { state ->
            if (state == MediaPlayerState.STATE_DEFAULT)
                return
            mediaPlayerInteractor.stop()
        }
        timerJob?.cancel()
    }

    fun pausePlayer() {
        playerState.value?.let { state ->
            if (state == MediaPlayerState.STATE_DEFAULT)
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

    fun changeFavoriteState() {
        if (trackInFavorite.value == true) {
            trackModel.value?.let { favoriteTrackInteractor.removeTrack(it) }
        } else {
            trackModel.value?.let { favoriteTrackInteractor.addTrack(it) }
        }
        trackInFavorite.postValue(trackInFavorite.value?.not())
    }

    fun loadPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists()
                .collect { list ->
                    if (list.isNullOrEmpty())
                        playlistState.postValue(PlaylistListState.Empty)
                    else
                        playlistState.postValue(PlaylistListState.Data(list))
                }
        }
    }

    override fun update() {
        loadPlaylists()
    }

    private fun checkTrackInFavorite(id: Int) {
        viewModelScope.launch {
            favoriteTrackInteractor.getInFavorites(id).collect{inFavorite ->
                trackInFavorite.postValue(inFavorite)
            }
        }
    }
    private fun checkTrackInFavorite() {
        trackModel.value?.let { track ->
            checkTrackInFavorite(track.trackId)
        }
    }

    fun clearMessage() {
        message.postValue(MessageState.Empty)
    }

    fun addToPlaylist(playlist: PlaylistModel) {
        trackModel.value?.let { track ->
            viewModelScope.launch {
                val contains = playlistInteractor.contains(playlist.uuid, track.trackId).first()
                Log.w(
                    "TAGGED",
                    "TRACK: ${playlist.name}, ${playlist.uuid}, ${trackModel.value?.trackId ?: "UNDEF"}, CONTAINS: $contains "
                )
                if (contains) {
                    Log.w(
                        "TAGGED",
                        "NEW TRACK: ${playlist.name}, ${playlist.uuid}, ${trackModel.value?.trackId ?: "UNDEF"}"
                    )
                    message.postValue(MessageState.TrackAlreadyInPlaylist(playlist.name))
                } else {
                    Log.w(
                        "TAGGED",
                        "TRACK EXISTS: ${playlist.name}, ${playlist.uuid}, ${trackModel.value?.trackId ?: "UNDEF"}"
                    )
                    playlistInteractor.addToPlaylist(playlist.uuid, track)
                    message.postValue(MessageState.AddTrackToPlaylistSuccess(playlist.name))
                }
            }
        }
    }
}