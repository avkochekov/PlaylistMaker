package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.search.data.model.Track
import av.kochekov.playlistmaker.search.domain.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TrackListInteractorImpl(private val repository: TrackListRepository) :
    TrackListInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.search(expression).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}