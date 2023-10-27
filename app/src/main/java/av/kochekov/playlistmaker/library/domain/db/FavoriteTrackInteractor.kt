package av.kochekov.playlistmaker.library.domain.db

import av.kochekov.playlistmaker.search.data.model.Track
import av.kochekov.playlistmaker.search.domain.model.TrackInfo
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {
    fun getTracks(): Flow<List<Track>>
    fun getTrackIds(): Flow<List<Int>>
    fun addTrack(track: Track)
    fun removeTrack(track: Track)
}