package av.kochekov.playlistmaker.search.presentation

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.*
import av.kochekov.playlistmaker.search.domain.SearchHistoryInteractor
import av.kochekov.playlistmaker.search.domain.TrackListInteractor
import av.kochekov.playlistmaker.common.data.models.Track
import av.kochekov.playlistmaker.search.domain.model.ErrorMessageType
import av.kochekov.playlistmaker.search.domain.model.SearchFragmentState
import av.kochekov.playlistmaker.search.domain.model.TrackModel
import av.kochekov.playlistmaker.search.data.utils.Mapper
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackListInteractor: TrackListInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val fragmentState = MutableLiveData<SearchFragmentState>()

    private val handler = Handler(Looper.getMainLooper())

    private var latestSearchText: String? = null

    fun fragmentState(): LiveData<SearchFragmentState> {
        return fragmentState
    }

    init {
        showHistory()
    }

    fun search(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        if (this.latestSearchText!!.isEmpty()) {
            showHistory()
            return
        }

        val searchRunnable = Runnable { showTrackList(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun search() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        latestSearchText?.let { showTrackList(it) }
    }

    fun breakSearch() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun addToHistory(track: TrackModel) {
        searchHistoryInteractor.add(Mapper.fromModel(track))
        if (fragmentState.value is SearchFragmentState.HistoryList)
            showHistory()
    }

    fun clearHistory() {
        searchHistoryInteractor.clear()
        showHistory()
    }

    private fun showTrackList(text: String) {
        fragmentState.value = SearchFragmentState.InSearchActivity
        viewModelScope.launch {
            trackListInteractor
                .searchTracks(text)
                .collect{
                    processResult(it.first, it.second)
                }
        }
    }

    private fun processResult(result: List<Track>?, errorMessage: String?) {
        if (!errorMessage.isNullOrEmpty()) {
            showErrorMessage(ErrorMessageType.NO_CONNECTION)
        } else if (result.isNullOrEmpty()){
            showErrorMessage(ErrorMessageType.NO_DATA)
        } else {
            showTracks(result)
        }
    }


    private fun showErrorMessage(type: ErrorMessageType) {
        fragmentState.value = SearchFragmentState.Error(type)
    }

    private fun showHistory() {
        fragmentState.value = SearchFragmentState.HistoryList(
            searchHistoryInteractor.get().map { Mapper.toModel(it) })
    }

    private fun showTracks(list: List<Track>) {
        fragmentState.value =
            SearchFragmentState.SearchResultList(list.map {
                Mapper.toModel(it)
            })
    }
}