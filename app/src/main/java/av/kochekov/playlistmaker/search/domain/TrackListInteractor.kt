package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.common.data.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackListInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>
}