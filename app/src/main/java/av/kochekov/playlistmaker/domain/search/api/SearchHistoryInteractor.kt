package av.kochekov.playlistmaker.domain.search.api

interface SearchHistoryInteractor {
    fun repository() : SearchHistoryRepositoryInterface
}