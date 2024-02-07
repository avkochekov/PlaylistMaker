package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.common.data.models.Track
import av.kochekov.playlistmaker.search.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface TrackListRepository {
    fun search(query: String): Flow<Resource<List<Track>>>
}