package av.kochekov.playlistmaker.domain.search.usecase

import av.kochekov.playlistmaker.domain.search.api.SearchHistoryRepositoryInterface
import av.kochekov.playlistmaker.domain.search.model.Track

class GetSearchHistoryUseCase(private val storage: SearchHistoryRepositoryInterface){

    fun execute() : List<Track>{
        return storage.getTrackList()
    }
}