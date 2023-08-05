package av.kochekov.playlistmaker.domain.search.usecase

import av.kochekov.playlistmaker.domain.search.api.SearchHistoryRepositoryInterface
import av.kochekov.playlistmaker.domain.search.model.Track

private const val MAX_LIST_SIZE = 10

class AddTrackToSearchHistoryUseCase(private val storage: SearchHistoryRepositoryInterface) {

    fun execute(track: Track){
        var trackList = storage.getTrackList().toMutableList()
        trackList.remove(track)
        while (trackList.size >= MAX_LIST_SIZE)
            trackList.removeFirst()
        trackList.add(track)
        storage.setTrackList(trackList)
    }
}