package av.kochekov.playlistmaker.library.presentation.favorite

import androidx.lifecycle.*
import av.kochekov.playlistmaker.favorite.domain.TrackRepositoryObserver
import av.kochekov.playlistmaker.library.domain.favorite.FavoriteTracksInteractor
import av.kochekov.playlistmaker.library.domain.favorite.models.TrackModel
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val interactor: FavoriteTracksInteractor
) : ViewModel(), TrackRepositoryObserver {
    private val state = MutableLiveData<FavoriteTrackListState>()

    fun activityState(): LiveData<FavoriteTrackListState> = state

    init {
        interactor.observe(this)
        load()
    }

    fun load() {
        renderState(FavoriteTrackListState.Loading)
        viewModelScope.launch {
            interactor.getTracks()
                .collect { list ->
                    processResult(list)
                }
        }
    }

    private fun processResult(tracks: List<TrackModel>) {
        if (tracks.isEmpty()) {
            renderState(FavoriteTrackListState.Empty)
        } else {
            renderState(FavoriteTrackListState.Content(tracks))
        }
    }

    private fun renderState(newState: FavoriteTrackListState) {
        state.postValue(newState)
    }

    override fun update() {
        load()
    }
}