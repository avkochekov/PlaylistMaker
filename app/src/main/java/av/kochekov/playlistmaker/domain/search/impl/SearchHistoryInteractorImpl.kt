package av.kochekov.playlistmaker.domain.search.impl

import av.kochekov.playlistmaker.domain.search.api.SearchHistoryInteractor
import av.kochekov.playlistmaker.domain.search.api.SearchHistoryRepositoryInterface
import av.kochekov.playlistmaker.domain.search.model.Track

private const val MAX_LIST_SIZE = 10

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepositoryInterface) : SearchHistoryInteractor {
    override fun add(track: Track) {
        var trackList = repository.getTrackList().toMutableList()
        trackList.remove(track)
        while (trackList.size >= MAX_LIST_SIZE)
            trackList.removeFirst()
        trackList.add(track)
        repository.setTrackList(trackList)
    }

    override fun get(): List<Track> {
        return repository.getTrackList()
    }

    override fun clear() {
        return repository.clear()
    }
}
