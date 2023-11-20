package av.kochekov.playlistmaker.library.domain.favorite

import av.kochekov.playlistmaker.favorite.domain.TrackRepositoryObserver
import av.kochekov.playlistmaker.favorite.domain.TrackRepository
import av.kochekov.playlistmaker.library.data.utils.Mapper
import av.kochekov.playlistmaker.library.domain.favorite.models.TrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksInteractorImpl(
    private val repository: TrackRepository
) : FavoriteTracksInteractor{
    override fun getTracks() : Flow<List<TrackModel>> = flow {
        repository.getFavoriteTracks().collect{list ->
            emit(list.map { data -> Mapper.toModel(data) })
        }
    }

    override fun observe(observer: TrackRepositoryObserver){
        repository.attach(observer)
    }
}