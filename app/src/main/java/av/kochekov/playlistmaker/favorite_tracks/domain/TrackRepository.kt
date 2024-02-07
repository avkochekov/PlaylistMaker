package av.kochekov.playlistmaker.favorite_tracks.domain

import av.kochekov.playlistmaker.common.data.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
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
}