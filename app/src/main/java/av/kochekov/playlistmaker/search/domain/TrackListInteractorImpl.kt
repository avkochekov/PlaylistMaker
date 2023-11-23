package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.favorite_tracks.data.utils.Mapper
import av.kochekov.playlistmaker.search.domain.model.Resource
import av.kochekov.playlistmaker.search.domain.model.TrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackListInteractorImpl(private val repository: TrackListRepository) :
    TrackListInteractor {

    override fun searchTracks(expression: String): Flow<Pair<List<TrackModel>?, String?>> = flow {
        repository.search(expression).collect { result ->
            emit(
                when (result) {
                    is Resource.Success -> {
                        Pair(result.data?.map { Mapper.toModel(it) }, null)
                    }
                    is Resource.Error -> {
                        Pair(null, result.message)
                    }
                }
            )
        }
    }
}