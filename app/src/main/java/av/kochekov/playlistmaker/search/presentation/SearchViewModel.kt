package av.kochekov.playlistmaker.search.presentation

import android.os.Handler
import android.os.Looper
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

    private val activityState = MutableLiveData<SearchActivityState>()
    private val searchText = MutableLiveData<String>()

    private val searchRunnable = Runnable { showTrackList() }
    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed: Boolean = true

    fun activityState() : LiveData<SearchActivityState> {
        return activityState
    }

    fun searchText() : LiveData<String> {
        return searchText
    }

    init {
        clearSearchText()
        showHistory()
    }

    fun search(){
        handler.removeCallbacks(searchRunnable)
        searchText.value?.let {
            if (it.isEmpty())
                showHistory()
            else
                searchRunnable.run()
        }
    }

    fun setSearchText(text: String){
        handler.removeCallbacks(searchRunnable)
        searchText.value = text
        if (text.isEmpty()) {
            showHistory()
        } else {
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
        }
    }

    fun clearSearchText(){
        setSearchText("")
    }

    fun addToHistory(track: TrackInfo){
        if (clickDebounce()) {
            searchHistoryInteractor.add(Mapper.fromTrackInfo(track))
            if (activityState.value is SearchActivityState.HistoryList)
                showHistory()
        }
    }

    fun clearHistory(){
        searchHistoryInteractor.clear()
        showHistory()
    }

    private fun showTrackList() {
        activityState.value = SearchActivityState.InSearchActivity
        searchText.value?.let {
            trackListInteractor.searchTracks(it, object : TrackListInteractor.TrackConsumer {
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
    }

    private fun showErrorMessage(type: ErrorMessageType){
        activityState.value = SearchActivityState.Error(type)
    }

    private fun showHistory(){
        activityState.value = SearchActivityState.HistoryList(searchHistoryInteractor.get().map{ Mapper.toTrackInfo(it) })
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
        return current
    }

    companion object {
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
}