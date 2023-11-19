package av.kochekov.playlistmaker.favorite_tracks.domain

import av.kochekov.playlistmaker.favorite.domain.FavoriteTrackRepositoryObserver
import av.kochekov.playlistmaker.search.domain.model.TrackModel
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {
    fun getTracks(): Flow<List<TrackModel>>
    fun getTrackIds(): Flow<List<Int>>
    fun getInFavorites(track: Int): Flow<Boolean>
    fun addTrack(track: TrackModel)
    fun removeTrack(track: TrackModel)
    fun observe(observer: FavoriteTrackRepositoryObserver)
}