package av.kochekov.playlistmaker.library.presentation.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import av.kochekov.playlistmaker.library.domain.playlists.models.PlaylistModel
import av.kochekov.playlistmaker.library.presentation.playlists.models.PlaylistState
import av.kochekov.playlistmaker.common.domain.PlaylistRepositoryObserver
import av.kochekov.playlistmaker.library.domain.playlists.PlaylistsInteractor
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val interactor: PlaylistsInteractor
) : ViewModel(), PlaylistRepositoryObserver {

    private val state = MutableLiveData<PlaylistState>()

    fun state(): LiveData<PlaylistState> = state

    init {
        interactor.observe(this)
        load()
    }

    private fun load() {
        renderState(PlaylistState.Loading)
        viewModelScope.launch {
            interactor.getPlaylists()
                .collect { list ->
                    processResult(list)
                }
        }
    }

    private fun processResult(list: List<PlaylistModel>) {
        if (list.isEmpty()) {
            renderState(PlaylistState.Empty)
        } else {
            renderState(PlaylistState.Content(list))
        }
    }

    private fun renderState(newState: PlaylistState) {
        state.postValue(newState)
    }

    override fun update() {
        load()
    }
}