package av.kochekov.playlistmaker.favorite.data

import av.kochekov.playlistmaker.favorite.data.converters.TrackDbConvertor
import av.kochekov.playlistmaker.favorite.data.db.AppDatabase
import av.kochekov.playlistmaker.favorite.data.db.entity.TrackEntity
import av.kochekov.playlistmaker.favorite.domain.FavoriteTrackRepository
import av.kochekov.playlistmaker.favorite.domain.FavoriteTrackRepositoryObserver
import av.kochekov.playlistmaker.search.data.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertor: TrackDbConvertor,
) : FavoriteTrackRepository {
    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override fun getTrackIds(): Flow<List<Int>> = flow {
        emit(appDatabase.trackDao().getTrackIds())
    }

    override suspend fun addTrack(track: Track) {
        appDatabase.trackDao().insertTrack(dbConvertor.map(track))
        update()
    }

    override suspend fun removeTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(dbConvertor.map(track))
        update()
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> dbConvertor.map(track) }
    }
}