package av.kochekov.playlistmaker.repository.search_repository.data

import av.kochekov.playlistmaker.repository.search_repository.domain.SearchHistoryInteractor
import av.kochekov.playlistmaker.repository.search_repository.domain.SearchHistoryRepository
import av.kochekov.playlistmaker.repository.track_list_repository.models.Track

private const val MAX_LIST_SIZE = 10

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
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
