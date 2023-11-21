package av.kochekov.playlistmaker.common.data.repository

import av.kochekov.playlistmaker.favorite.data.converters.TrackDbConvertor
import av.kochekov.playlistmaker.common.data.db.AppDatabase
import av.kochekov.playlistmaker.favorite_tracks.domain.TrackRepository
import av.kochekov.playlistmaker.common.data.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: TrackDbConvertor,
) : TrackRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val list = appDatabase.favoriteTracks().getTracks()
        emit(list.map { track -> convertor.map(track) })
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val list = appDatabase.trackDao().getTracks()
        emit(list.map { track -> convertor.map(track) })
    }

    override fun getTrack(id: Int): Flow<Track?> = flow {
        val list = appDatabase.trackDao().getTrack(id)
        if (list.isEmpty() || list.size > 1)
            emit(null)
        emit(convertor.map(list.first()))
    }

    override fun getFavoriteTrackIds(): Flow<List<Int>> = flow {
        val list = appDatabase.favoriteTracks().getTrackIds()
        emit(list)
    }

    override fun contains(id: Int): Flow<Boolean> = flow {
        emit(appDatabase.trackDao().getTrack(id).isNotEmpty())
    }

    override fun containsInFavorite(track: Track): Flow<Boolean> = flow {
        containsInFavorite(track.trackId)
    }

    override fun containsInFavorite(id: Int): Flow<Boolean> = flow {
        emit(appDatabase.favoriteTracks().contains(id).isNotEmpty())
    }

    override suspend fun addToFavorite(track: Track) {
        appDatabase.trackDao().insertTrack(convertor.map(track))
        appDatabase.favoriteTracks().insertTrack(track.trackId)
    }

    override suspend fun removeFromFavorite(track: Track) {
        removeFromFavorite(track.trackId)
    }

    override suspend fun removeFromFavorite(trackId: Int) {
        appDatabase.favoriteTracks().deleteTrack(trackId)
        appDatabase.garbageCollectorDao().clearTracks()
    }
}