package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.search.data.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackListInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>
}