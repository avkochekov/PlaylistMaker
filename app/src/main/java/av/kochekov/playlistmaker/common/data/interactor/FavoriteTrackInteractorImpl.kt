package av.kochekov.playlistmaker.common.data.interactor

import av.kochekov.playlistmaker.favorite.domain.FavoriteTrackRepository
import av.kochekov.playlistmaker.favorite.domain.FavoriteTrackRepositoryObserver
import av.kochekov.playlistmaker.favorite_tracks.data.utils.Mapper
import av.kochekov.playlistmaker.favorite_tracks.domain.FavoriteTrackInteractor
import av.kochekov.playlistmaker.search.domain.model.TrackModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTrackInteractorImpl(
    private val repository: FavoriteTrackRepository
) : FavoriteTrackInteractor {
    override fun getTracks(): Flow<List<TrackModel>> = flow {
        repository.getTracks().collect { list ->
            emit(list.map { data -> Mapper.toModel(data) })
        }
    }

    override fun getTrackIds(): Flow<List<Int>> {
        return repository.getTrackIds()
    }

    override fun getInFavorites(track: Int): Flow<Boolean> {
        return repository.containsTrack(track)
    }

    override fun addTrack(data: TrackModel) {
        GlobalScope.async { repository.addTrack(Mapper.fromModel(data)) }
    }

    override fun removeTrack(data: TrackModel) {
        GlobalScope.async { repository.removeTrack(Mapper.fromModel(data)) }
    }

    override fun observe(observer: FavoriteTrackRepositoryObserver) {
        repository.attach(observer)
    }

}