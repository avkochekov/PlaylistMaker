package av.kochekov.playlistmaker.search.presentation

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.*
import av.kochekov.playlistmaker.search.domain.SearchHistoryInteractor
import av.kochekov.playlistmaker.search.domain.TrackListInteractor
import av.kochekov.playlistmaker.search.data.model.Track
import av.kochekov.playlistmaker.search.domain.model.ErrorMessageType
import av.kochekov.playlistmaker.search.domain.model.SearchActivityState
import av.kochekov.playlistmaker.search.domain.model.TrackInfo
import av.kochekov.playlistmaker.search.data.utils.Mapper
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackListInteractor: TrackListInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getSearchModelFactory(
            trackListInteractor: TrackListInteractor,
            searchHistoryInteractor: SearchHistoryInteractor
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(
                        trackListInteractor = trackListInteractor,
                        searchHistoryInteractor = searchHistoryInteractor
                    ) as T
                }
            }
    }

    private val activityState = MutableLiveData<SearchActivityState>()

    private val handler = Handler(Looper.getMainLooper())

    private var latestSearchText: String? = null

    fun activityState(): LiveData<SearchActivityState> {
        return activityState
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

    fun addToHistory(track: TrackInfo) {
        searchHistoryInteractor.add(Mapper.fromTrackInfo(track))
        if (activityState.value is SearchActivityState.HistoryList)
            showHistory()
    }

    fun clearHistory() {
        searchHistoryInteractor.clear()
        showHistory()
    }

    private fun showTrackList(text: String) {
        activityState.value = SearchActivityState.InSearchActivity
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
        activityState.value = SearchActivityState.Error(type)
    }

    private fun showHistory() {
        activityState.value = SearchActivityState.HistoryList(
            searchHistoryInteractor.get().map { Mapper.toTrackInfo(it) })
    }

    private fun showTracks(list: List<Track>) {
        activityState.value =
            SearchActivityState.SearchResultList(list.map {
                Mapper.toTrackInfo(it)
            })
    }
}