package av.kochekov.playlistmaker.library.data

import av.kochekov.playlistmaker.library.data.converters.TrackDbConvertor
import av.kochekov.playlistmaker.library.data.db.AppDatabase
import av.kochekov.playlistmaker.library.data.db.entity.TrackEntity
import av.kochekov.playlistmaker.library.domain.FavoriteTrackRepository
import av.kochekov.playlistmaker.search.data.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class FavoriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertor: TrackDbConvertor
) : FavoriteTrackRepository{
    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override fun getTrackIds(): Flow<List<Int>> = flow {
        emit(appDatabase.trackDao().getTrackIds())
    }

    override fun addTrack(track: Track) {
        GlobalScope.async {
            appDatabase.trackDao().insertTrack(dbConvertor.map(track))
        }
    }

    override fun removeTrack(track: Track) {
        GlobalScope.async {
            appDatabase.trackDao().deleteTrack(dbConvertor.map(track))
        }
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> dbConvertor.map(track) }
    }
}