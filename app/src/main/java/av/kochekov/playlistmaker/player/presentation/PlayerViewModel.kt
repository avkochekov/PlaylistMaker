package av.kochekov.playlistmaker.player.presentation

import androidx.lifecycle.*
import av.kochekov.playlistmaker.favorite_tracks.domain.TrackInteractor
import av.kochekov.playlistmaker.player.domain.models.PlaylistListState
import av.kochekov.playlistmaker.player.presentation.models.MessageState
import av.kochekov.playlistmaker.playlist_editor.domain.PlaylistInteractor
import av.kochekov.playlistmaker.playlist_editor.domain.models.PlaylistModel
import av.kochekov.playlistmaker.common.domain.TrackModel
import av.kochekov.playlistmaker.services.AudioPlayerControl
import av.kochekov.playlistmaker.services.NotificationControl
import av.kochekov.playlistmaker.services.PlayerState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

private const val TIME_UPDATE_VALUE_MILLIS = 300L
private const val DEFAULT_TRACK_POSITION = 0

class PlayerViewModel(
    private val favoriteTrackInteractor: TrackInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val trackInteractor: TrackInteractor
) : ViewModel() {

    private var trackModel = MutableLiveData<TrackModel>()
    private var trackInFavorite = MutableLiveData<Boolean>()

    private var message = MutableLiveData<MessageState>()

    private var playlistState = MutableLiveData<PlaylistListState>()

    private var timerJob: Job? = null

    init {
        checkTrackInFavorite()
        loadPlaylists()
    }

    private val _playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun playerState(): LiveData<PlayerState> = _playerState

    private var audioPlayerControl: AudioPlayerControl? = null
    private var notificationControl: NotificationControl? = null

    fun setAudioPlayerControl(control: AudioPlayerControl) {
        audioPlayerControl = control

        viewModelScope.launch {
            audioPlayerControl?.let {control ->
                control.getPlayerState().collect {state ->
                    _playerState.postValue(state)
                }
            }
        }
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    fun setNotificationControl(control: NotificationControl) {
        notificationControl = control
    }

    fun removeNotificationControl() {
        notificationControl = null
    }

    fun onPlayerButtonClicked() {
        if (_playerState.value is PlayerState.Playing) {
            audioPlayerControl?.pausePlayer()
        } else {
            audioPlayerControl?.startPlayer()
        }
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
        checkTrackInFavorite(track.trackId)
    }

    fun setTrack(track: Int) {
        viewModelScope.async {
            trackInteractor.getTrack(track).collect { data ->
                data?.let { setTrack(it) }
            }
        }
    }
    fun changeFavoriteState() {
        trackModel.value?.let { data ->
            val inFavorite = trackInFavorite.value == true
            if (inFavorite) {
                favoriteTrackInteractor.removeTrack(data)
            } else {
                favoriteTrackInteractor.addTrack(data)
            }
            trackInFavorite.postValue(inFavorite.not())
        }
    }

    fun loadPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { setPlaylists(it) }
        }
    }

    private fun setPlaylists(list: List<PlaylistModel>) {
        if (list.isNullOrEmpty())
            playlistState.postValue(PlaylistListState.Empty)
        else
            playlistState.postValue(PlaylistListState.Data(list))
    }

    private fun checkTrackInFavorite(id: Int) {
        viewModelScope.launch {
            favoriteTrackInteractor.getInFavorites(id).collect { inFavorite ->
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
                if (contains) {
                    message.postValue(MessageState.TrackAlreadyInPlaylist(playlist.name))
                } else {
                    playlistInteractor.addToPlaylist(playlist.uuid, track)
                        .collect { setPlaylists(it) }
                    message.postValue(MessageState.AddTrackToPlaylistSuccess(playlist.name))
                }
            }
        }
    }

    fun onPause(){
        if (playerState().value is PlayerState.Playing)
            notificationControl?.showNotification()
    }

    fun onResume(){
        notificationControl?.hideNotification()
    }

}