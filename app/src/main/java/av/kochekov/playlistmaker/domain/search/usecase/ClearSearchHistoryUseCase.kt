package av.kochekov.playlistmaker.domain.search.usecase

import av.kochekov.playlistmaker.domain.search.api.SearchHistoryRepositoryInterface

class ClearSearchHistoryUseCase (private val storage: SearchHistoryRepositoryInterface) {

    fun execute(){
        storage.clear()
    }
}