package av.kochekov.playlistmaker.domain.search.impl

import av.kochekov.playlistmaker.domain.search.api.SearchHistoryInteractor
import av.kochekov.playlistmaker.domain.search.api.SearchHistoryRepositoryInterface

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepositoryInterface) : SearchHistoryInteractor {
    override fun repository(): SearchHistoryRepositoryInterface {
        return repository
    }
}
