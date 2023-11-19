package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.search.domain.model.TrackModel
import kotlinx.coroutines.flow.Flow

interface TrackListInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<TrackModel>?, String?>>
}