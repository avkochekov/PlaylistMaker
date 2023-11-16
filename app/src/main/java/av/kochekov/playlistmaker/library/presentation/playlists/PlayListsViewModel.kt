package av.kochekov.playlistmaker.library.presentation.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import av.kochekov.playlistmaker.playlist.domain.PlaylistInteractor
import av.kochekov.playlistmaker.playlist.domain.PlaylistRepositoryObserver
import av.kochekov.playlistmaker.common.data.models.Playlist
import kotlinx.coroutines.launch

class PlayListsViewModel(
    private val interactor: PlaylistInteractor
) : ViewModel(), PlaylistRepositoryObserver {

    private val state = MutableLiveData<PlayListState>()

    fun state(): LiveData<PlayListState> = state

    init {
        interactor.observe(this)
        load()
    }

    fun load(){
        renderState(PlayListState.Loading)
        viewModelScope.launch {
            interactor.getPlaylists()
                .collect { list ->
                    processResult(list)
                }
        }
    }

    private fun processResult(list: List<Playlist>) {
        if (list.isEmpty()) {
            renderState(PlayListState.Empty)
        } else {
            renderState(PlayListState.Content(list))
        }
    }
    private fun renderState(newState: PlayListState) {
        state.postValue(newState)
    }

    override fun update() {
        load()
    }
}