package av.kochekov.playlistmaker.favorite.domain

import av.kochekov.playlistmaker.common.data.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepositoryObserver {
    fun update()
}

interface TrackRepository {
    companion object {
        private var observerList: MutableList<TrackRepositoryObserver> = mutableListOf()
    }

    fun getFavoriteTracks(): Flow<List<Track>>;

    fun getTracks(): Flow<List<Track>>

    fun getTrack(id: Int): Flow<Track?>;

    fun getFavoriteTrackIds(): Flow<List<Int>>

    fun contains(trackId: Int): Flow<Boolean>

    fun containsInFavorite(track: Track): Flow<Boolean>

    fun containsInFavorite(trackId: Int): Flow<Boolean>

    suspend fun addToFavorite(track: Track)

    suspend fun removeFromFavorite(track: Track)

    suspend fun removeFromFavorite(trackId: Int)
    fun attach(observer: TrackRepositoryObserver) {
        observerList.add(observer)
    }

    fun update() {
        observerList.map {
            it.update()
        }
    }
}