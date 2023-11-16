package av.kochekov.playlistmaker.search.domain.model

sealed interface SearchFragmentState {

    data class HistoryList(
        val trackList: List<TrackModel>
    ) : SearchFragmentState

    object InSearchActivity : SearchFragmentState

    data class SearchResultList(
        val trackList: List<TrackModel>
    ) : SearchFragmentState

    data class Error(
        val error: ErrorMessageType
    ) : SearchFragmentState
}
