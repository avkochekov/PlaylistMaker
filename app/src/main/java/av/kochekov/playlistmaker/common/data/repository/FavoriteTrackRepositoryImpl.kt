package av.kochekov.playlistmaker.common.data.repository

import av.kochekov.playlistmaker.favorite.data.converters.TrackDbConvertor
import av.kochekov.playlistmaker.common.data.db.AppDatabase
import av.kochekov.playlistmaker.common.data.db.relationships.FavoriteTrackRelation
import av.kochekov.playlistmaker.favorite.domain.FavoriteTrackRepository
import av.kochekov.playlistmaker.common.data.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: TrackDbConvertor,
) : FavoriteTrackRepository {
    override fun getTracks(): Flow<List<Track>> = flow {
        val list = appDatabase.favoriteTracks().getTracks()
        emit(convertFromFavoriteRelation(list))
    }

    override fun getTrackIds(): Flow<List<Int>> = flow {
        val list = appDatabase.favoriteTracks().getTrackIds()
        emit(list)
    }

    override fun containsTrack(track: Track): Flow<Boolean> = flow {
        containsTrack(track.trackId)
    }

    override fun containsTrack(id: Int): Flow<Boolean> = flow {
        emit(appDatabase.favoriteTracks().contains(id).isNotEmpty())
    }

    override suspend fun addTrack(track: Track) {
        appDatabase.trackDao().insertTrack(convertor.map(track))
        appDatabase.favoriteTracks().insertTrack(track.trackId)
        update()
    }

    override suspend fun removeTrack(track: Track) {
        removeTrack(track.trackId)
    }

    override suspend fun removeTrack(trackId: Int) {
        appDatabase.favoriteTracks().deleteTrack(trackId)
        update()
    }

    private fun convertFromFavoriteRelation(tracks: List<FavoriteTrackRelation>): List<Track> {
        return tracks.map { track -> convertor.map(track) }
    }
}