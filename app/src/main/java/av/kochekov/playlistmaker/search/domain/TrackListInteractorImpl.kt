package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.search.domain.TrackListInteractor
import av.kochekov.playlistmaker.search.domain.TrackListRepository
import java.util.concurrent.Executors

class TrackListInteractorImpl(private val repository: TrackListRepository) :
    TrackListInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TrackListInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.search(expression))
        }
    }
}