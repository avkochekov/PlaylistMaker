package av.kochekov.playlistmaker.library.domain

import av.kochekov.playlistmaker.search.data.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackRepository {
    fun getTracks(): Flow<List<Track>>;

    fun getTrackIds(): Flow<List<Int>>

    fun addTrack(track: Track)

    fun removeTrack(track: Track)
}