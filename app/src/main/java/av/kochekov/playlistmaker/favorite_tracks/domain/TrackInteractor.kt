package av.kochekov.playlistmaker.favorite_tracks.domain

import av.kochekov.playlistmaker.favorite.domain.TrackRepositoryObserver
import av.kochekov.playlistmaker.search.domain.model.TrackModel
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun getTracks(): Flow<List<TrackModel>>
    fun getTrack(id: Int): Flow<TrackModel?>
    fun getTrackIds(): Flow<List<Int>>
    fun getInFavorites(track: Int): Flow<Boolean>
    fun addTrack(track: TrackModel)
    fun removeTrack(track: TrackModel)
    fun observe(observer: TrackRepositoryObserver)
}