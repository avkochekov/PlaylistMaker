package av.kochekov.playlistmaker.playlist_view.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import av.kochekov.playlistmaker.common.domain.PlaylistRepositoryObserver
import av.kochekov.playlistmaker.playlist_view.domain.PlaylistViewInteractor
import av.kochekov.playlistmaker.playlist_view.domain.SharingInteractor
import av.kochekov.playlistmaker.playlist_view.domain.models.PlaylistModel
import av.kochekov.playlistmaker.playlist_view.domain.models.TrackModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class PlaylistViewViewModel(
    private val playlistInteractor: PlaylistViewInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel(), PlaylistRepositoryObserver {
    private val playlistData = MutableLiveData<PlaylistModel>()

    fun playlistData(): LiveData<PlaylistModel> {
        return playlistData
    }

    init {
        playlistInteractor.observe(this)
    }

    fun load(uuid: String) {
        GlobalScope.async {
            playlistInteractor.getPlaylist(uuid).collect { data ->
                data?.let { data ->
                    playlistData.postValue(data)
                }
            }
        }
    }

    fun removeTrack(track: TrackModel) {
        playlistData.value?.let { playlist ->
            playlistInteractor.removeTrack(playlist.uuid, track.trackId)
        }
    }

    fun removePlaylist(){
        playlistData.value?.let { playlist ->
            playlistInteractor.removePlaylist(playlist.uuid)
        }
    }

    override fun update() {
        playlistData.value?.let { playlist ->
            load(playlist.uuid)
        }
    }

    fun share() {
        playlistData.value?.let { sharingInteractor.sharePlaylist(it) }
    }
}