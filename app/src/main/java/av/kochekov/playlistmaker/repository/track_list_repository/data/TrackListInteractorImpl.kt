package av.kochekov.playlistmaker.repository.track_list_repository.data

import av.kochekov.playlistmaker.repository.track_list_repository.domain.TrackListInteractor
import av.kochekov.playlistmaker.repository.track_list_repository.domain.TrackListRepository
import java.util.concurrent.Executors

class TrackListInteractorImpl (private val repository: TrackListRepository) :
    TrackListInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TrackListInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.search(expression))
        }
    }
}