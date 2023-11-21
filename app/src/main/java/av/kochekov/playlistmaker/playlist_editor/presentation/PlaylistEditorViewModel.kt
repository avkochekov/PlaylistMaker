package av.kochekov.playlistmaker.playlist_editor.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import av.kochekov.playlistmaker.playlist_editor.domain.PlaylistInteractor
import av.kochekov.playlistmaker.playlist_editor.domain.models.PlaylistModel
import av.kochekov.playlistmaker.playlist_editor.presentation.models.PlaylistEditorState
import kotlinx.coroutines.async

class PlaylistEditorViewModel(
    private val interactor: PlaylistInteractor
) : ViewModel() {
    private val fragmentState = MutableLiveData<PlaylistEditorState>()
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
            PlaylistEditorState.Data(initState!!.copy())
        )
    }

    fun load(uuid: String) {
        viewModelScope.async {
            interactor.getPlaylist(uuid).collect {
                it?.let {
                    fragmentState.postValue(PlaylistEditorState.Data(it))
                    initState = it.copy()
                }
            }
        }
    }

    fun setArtwork(path: String) {
        with(fragmentState.value as PlaylistEditorState.Data){
            if (path != data.artwork)
                data.artwork = path
        }
    }

    fun setName(text: String) {
         with(fragmentState.value as PlaylistEditorState.Data){
            if (text != data.name)
                data.name = text
        }
    }

    fun setDescription(text: String) {
        with(fragmentState.value as PlaylistEditorState.Data){
            if (text != data.description)
                data.description = text
        }
    }

    fun fragmentState(): LiveData<PlaylistEditorState> {
        return fragmentState
    }

    fun savePlayList() {
        when (fragmentState.value) {
            is PlaylistEditorState.Data -> {
                val data = fragmentState.value as PlaylistEditorState.Data
                viewModelScope.async {
                    interactor.savePlaylist(data.data).collect { data ->
                        data?.let {
                            fragmentState.postValue(PlaylistEditorState.Saved(it))
                            initState = data.copy()
                        }
                    }
                }
            }
            else -> {
                // Do nothing
            }

        }
        fragmentState.value?.let { data ->

        }
    }

    fun isChanged(): Boolean {
        return when (val data = fragmentState.value) {
            is PlaylistEditorState.Data -> {
                isChanged((data as PlaylistEditorState.Data).data)
            }
            is PlaylistEditorState.Saved -> {
                isChanged((data as PlaylistEditorState.Saved).data)
            }
            else -> {
                false
            }

        }
    }

    private fun isChanged(data: PlaylistModel): Boolean {
        if (data.artwork != initState?.artwork)
            return true
        if (data.name != initState?.name)
            return true
        if (data.description != initState?.description)
            return true
        return false
    }

    fun isNewPlaylist(): Boolean {
        return when(fragmentState.value){
            is PlaylistEditorState.Data -> {
                (fragmentState.value as PlaylistEditorState.Data).data.uuid.isEmpty()
            }
            is PlaylistEditorState.Saved -> {
                (fragmentState.value as PlaylistEditorState.Saved).data.uuid.isEmpty()
            }
            else -> {
                true
            }
        }
    }
}