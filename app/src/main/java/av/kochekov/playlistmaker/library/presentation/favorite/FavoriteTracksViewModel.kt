package av.kochekov.playlistmaker.library.presentation.favorite

import android.content.Context
import androidx.lifecycle.*
import av.kochekov.playlistmaker.library.domain.db.FavoriteTrackInteractor
import av.kochekov.playlistmaker.search.data.utils.Mapper
import av.kochekov.playlistmaker.search.domain.model.TrackInfo
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val interactor: FavoriteTrackInteractor
) : ViewModel()  {
    private val state = MutableLiveData<FavoriteTrackListState>()

    fun activityState(): LiveData<FavoriteTrackListState> = state

    init {
        load()
    }

    fun load(){
        renderState(FavoriteTrackListState.Loading)
        viewModelScope.launch {
            interactor.getTracks()
                .collect { tracks ->
                    processResult(tracks.map {
                        Mapper.toTrackInfo(it)
                    })
                }
        }
    }

    private fun processResult(tracks: List<TrackInfo>) {
        if (tracks.isEmpty()) {
            renderState(FavoriteTrackListState.Empty)
        } else {
            renderState(FavoriteTrackListState.Content(tracks))
        }
    }

    private fun renderState(newState: FavoriteTrackListState) {
        state.postValue(newState)
    }
}