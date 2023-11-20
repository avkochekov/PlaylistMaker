package av.kochekov.playlistmaker.playlist_editor.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import av.kochekov.playlistmaker.playlist_editor.domain.PlaylistInteractor
import av.kochekov.playlistmaker.playlist_editor.domain.models.PlaylistModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect

class PlaylistEditorViewModel(
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

    fun setPlaylist(uuid: String){
        GlobalScope.async {
            interactor.getPlaylist(uuid).collect{
                it?.let {
                    initState = it.copy()
                    fragmentState.postValue(it)
                }
            }
        }
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

    fun isNewPlaylist() : Boolean{
        return fragmentState.value?.uuid?.isEmpty()?:true
    }
}