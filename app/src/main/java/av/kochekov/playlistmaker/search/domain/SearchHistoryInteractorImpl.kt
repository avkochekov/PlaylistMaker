package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.search.data.model.Track

private const val MAX_LIST_SIZE = 10

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun add(track: Track) {
        var trackList = repository.getTrackList().toMutableList()
        trackList.remove(track)
        while (trackList.size >= MAX_LIST_SIZE)
            trackList.removeFirst()
        trackList.add(0, track)
        repository.setTrackList(trackList)
    }

    override fun get(): List<Track> {
        return repository.getTrackList()
    }

    override fun clear() {
        return repository.clear()
    }
}
