package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.common.domain.TrackModel
import kotlinx.coroutines.flow.Flow

interface TrackListInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<TrackModel>?, String?>>
}