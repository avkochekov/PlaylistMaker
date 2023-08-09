package av.kochekov.playlistmaker.search.models

sealed interface SearchActivityState {

    data class HistoryList(
        val trackList: List<TrackInfo>
    ) : SearchActivityState

    object InSearchActivity : SearchActivityState

    data class SearchResultList(
        val trackList: List<TrackInfo>
    ) : SearchActivityState

    data class Error (
        val error: ErrorMessageType
    ): SearchActivityState
}
