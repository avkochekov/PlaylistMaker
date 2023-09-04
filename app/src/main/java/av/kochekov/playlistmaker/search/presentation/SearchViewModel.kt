package av.kochekov.playlistmaker.search.presentation

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import av.kochekov.playlistmaker.search.domain.SearchHistoryInteractor
import av.kochekov.playlistmaker.search.domain.TrackListInteractor
import av.kochekov.playlistmaker.search.data.model.Track
import av.kochekov.playlistmaker.search.domain.model.ErrorMessageType
import av.kochekov.playlistmaker.search.domain.model.SearchActivityState
import av.kochekov.playlistmaker.search.domain.model.TrackInfo
import av.kochekov.playlistmaker.search.data.utils.Mapper

private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L

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
        ) : ViewModelProvider.Factory =
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
    private var isClickAllowed: Boolean = true

    fun activityState() : LiveData<SearchActivityState> {
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

        if (this.latestSearchText!!.isEmpty()){
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

    fun search(){
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        latestSearchText?.let { showTrackList(it) }
    }

    fun addToHistory(track: TrackInfo){
        searchHistoryInteractor.add(Mapper.fromTrackInfo(track))
        if (activityState.value is SearchActivityState.HistoryList)
            showHistory()
    }

    fun clearHistory(){
        searchHistoryInteractor.clear()
        showHistory()
    }

    private fun showTrackList(text: String) {
        activityState.value = SearchActivityState.InSearchActivity
        trackListInteractor.searchTracks(text, object : TrackListInteractor.TrackConsumer {
            override fun consume(foundTracks: List<Track>?) {
                handler.post {
                    foundTracks?.let { it ->
                        if (it.isEmpty()) {
                            showErrorMessage(ErrorMessageType.NO_DATA)
                        } else {
                            activityState.value =
                                SearchActivityState.SearchResultList(foundTracks.map {
                                    Mapper.toTrackInfo(it)
                                })
                        }
                    }
                        ?: showErrorMessage(ErrorMessageType.NO_CONNECTION)
                }
            }
        })
    }

    private fun showErrorMessage(type: ErrorMessageType){
        activityState.value = SearchActivityState.Error(type)
    }

    private fun showHistory(){
        activityState.value = SearchActivityState.HistoryList(searchHistoryInteractor.get().map{ Mapper.toTrackInfo(it) })
    }

    fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
        return current
    }
}