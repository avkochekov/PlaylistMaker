package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.search.data.utils.Mapper
import av.kochekov.playlistmaker.search.domain.model.TrackModel

private const val MAX_LIST_SIZE = 10

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun add(track: TrackModel) {
        Mapper.fromModel(track).let { track ->
            var trackList = repository.getTrackList().toMutableList()
            trackList.remove(track)
            while (trackList.size >= MAX_LIST_SIZE)
                trackList.removeFirst()
            trackList.add(0, track)
            repository.setTrackList(trackList)
        }
    }

    override fun get(): List<TrackModel> {
        return repository.getTrackList().map { track -> Mapper.toModel(track) }
    }

    override fun clear() {
        return repository.clear()
    }
}
