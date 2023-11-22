package av.kochekov.playlistmaker.playlist_view.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import av.kochekov.playlistmaker.playlist_view.domain.PlaylistViewInteractor
import av.kochekov.playlistmaker.playlist_view.domain.SharingInteractor
import av.kochekov.playlistmaker.playlist_view.domain.models.PlaylistModel
import av.kochekov.playlistmaker.playlist_view.domain.models.TrackModel
import kotlinx.coroutines.async

class PlaylistViewViewModel(
    private val playlistInteractor: PlaylistViewInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private val playlistData = MutableLiveData<PlaylistModel>()

    fun playlistData(): LiveData<PlaylistModel> {
        return playlistData
    }

    fun load(uuid: String) {
        viewModelScope.async {
            playlistInteractor.getPlaylist(uuid).collect { data ->
                data?.let { setPlaylistData(it) }
            }
        }
    }

    fun removeTrack(track: TrackModel) {
        playlistData.value?.let { playlist ->
            viewModelScope.async {
                playlistInteractor.removeTrack(playlist.uuid, track.trackId)
                    .collect { setPlaylistData(it) }

            }
        }
    }

    fun removePlaylist() {
        playlistData.value?.let { playlist ->
            viewModelScope.async {
                playlistInteractor.removePlaylist(playlist.uuid)
            }
        }
    }

    fun share(data: String) {
        playlistData.value?.let {
            sharingInteractor.sharePlaylist( data ) }
    }

    private fun setPlaylistData(data: PlaylistModel) {
        playlistData.postValue(data)
    }
}