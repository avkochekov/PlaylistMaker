package av.kochekov.playlistmaker.common.data.interactor

import av.kochekov.playlistmaker.favorite.domain.TrackRepository
import av.kochekov.playlistmaker.favorite.domain.TrackRepositoryObserver
import av.kochekov.playlistmaker.favorite_tracks.data.utils.Mapper
import av.kochekov.playlistmaker.favorite_tracks.domain.TrackInteractor
import av.kochekov.playlistmaker.search.domain.model.TrackModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackInteractorImpl(
    private val repository: TrackRepository
) : TrackInteractor {
    override fun getTracks(): Flow<List<TrackModel>> = flow {
        repository.getFavoriteTracks().collect { list ->
            emit(list.map { data -> Mapper.toModel(data) })
        }
    }

    override fun getTrack(id: Int): Flow<TrackModel?> = flow {
        repository.getTrack(id).collect { data ->
            data?.let {
                emit(Mapper.toModel(data))
            } ?: emit(null)
        }
    }

    override fun getTrackIds(): Flow<List<Int>> {
        return repository.getFavoriteTrackIds()
    }

    override fun getInFavorites(track: Int): Flow<Boolean> {
        return repository.containsInFavorite(track)
    }

    override fun addTrack(data: TrackModel) {
        GlobalScope.async { repository.addToFavorite(Mapper.fromModel(data)) }
    }

    override fun removeTrack(data: TrackModel) {
        GlobalScope.async { repository.removeFromFavorite(Mapper.fromModel(data)) }
    }

    override fun observe(observer: TrackRepositoryObserver) {
        repository.attach(observer)
    }

}