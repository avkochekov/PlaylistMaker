package av.kochekov.playlistmaker.library.domain.favorite

import av.kochekov.playlistmaker.library.domain.favorite.models.TrackModel
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    fun getTracks() : Flow<List<TrackModel>>
}