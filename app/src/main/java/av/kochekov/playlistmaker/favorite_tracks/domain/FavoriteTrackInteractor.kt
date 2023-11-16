package av.kochekov.playlistmaker.favorite_tracks.domain

import av.kochekov.playlistmaker.favorite.domain.FavoriteTrackRepositoryObserver
import av.kochekov.playlistmaker.common.data.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {
    fun getTracks(): Flow<List<Track>>
    fun getTrackIds(): Flow<List<Int>>
    fun getInFavorites(track: Int): Flow<Boolean>
    fun addTrack(track: Track)
    fun removeTrack(track: Track)



    fun observe(observer: FavoriteTrackRepositoryObserver)
}