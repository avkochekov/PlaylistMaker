package av.kochekov.playlistmaker.domain.search.impl

import av.kochekov.playlistmaker.domain.search.api.TrackListInteractor
import av.kochekov.playlistmaker.domain.search.api.TrackListRepositoryInterface
import java.util.concurrent.Executors

class TrackListInteractorImpl (private val repository: TrackListRepositoryInterface) : TrackListInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TrackListInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.search(expression))
        }
    }
}