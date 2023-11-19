package av.kochekov.playlistmaker.playlist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import av.kochekov.playlistmaker.playlist.domain.PlaylistInteractor
import av.kochekov.playlistmaker.playlist.domain.models.PlaylistModel

class PlaylistViewModel(
    private val interactor: PlaylistInteractor
) : ViewModel() {
    private val fragmentState = MutableLiveData<PlaylistModel>()
    private var initState: PlaylistModel? = null

    init {
        initState = PlaylistModel(
            uuid = "",
            artwork = "",
            name = "",
            description = "",
            tracksCount = 0
        )
        fragmentState.postValue(
            initState?.copy()
        )
    }

    fun setArtwork(path: String) {
        if (path != fragmentState.value?.artwork)
            fragmentState.value?.artwork = path
    }

    fun setName(text: String) {
        if (text != fragmentState.value?.name)
            fragmentState.value?.name = text
    }

    fun setDescription(text: String) {
        if (text != fragmentState.value?.description)
            fragmentState.value?.description = text
    }

    fun fragmentState(): LiveData<PlaylistModel> {
        return fragmentState
    }

    fun savePlayList() {
        fragmentState.value?.apply {
            interactor.savePlaylist(this)
        }
        initState = fragmentState.value?.copy()
    }

    fun isChanged(): Boolean {
        if (fragmentState.value?.artwork != initState?.artwork)
            return true
        if (fragmentState.value?.name != initState?.name)
            return true
        if (fragmentState.value?.description != initState?.description)
            return true
        return false
    }
}