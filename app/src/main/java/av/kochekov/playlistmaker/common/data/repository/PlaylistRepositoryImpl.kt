package av.kochekov.playlistmaker.common.data.repository

import av.kochekov.playlistmaker.common.data.db.AppDatabase
import av.kochekov.playlistmaker.favorite.data.converters.TrackDbConvertor
import av.kochekov.playlistmaker.common.data.converters.PlaylistDbConvertor
import av.kochekov.playlistmaker.playlist.domain.PlaylistRepository
import av.kochekov.playlistmaker.common.data.models.Playlist
import av.kochekov.playlistmaker.common.data.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistConvertor: PlaylistDbConvertor,
    private val trackConvertor: TrackDbConvertor,
    ) : PlaylistRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistConvertor.map(playlist))
        update()
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistConvertor.map(playlist))
        update()
    }

    override suspend fun removePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().removePlaylist(playlistConvertor.map(playlist))
        update()
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow{
        val list = appDatabase.playlistDao().getPlaylistsWithTracks()
        emit(list.map { model -> playlistConvertor.map(model) })
    }

    override fun getPlaylist(udi: String): Flow<Playlist?> = flow {
        val list = appDatabase.playlistDao().getPlaylist(udi)
        if (list.isEmpty() || list.size > 1)
            emit(null)
        else
            emit(playlistConvertor.map(list.first()))
    }

    override fun containsPlaylists(udi: String): Flow<Boolean> = flow {
        emit(appDatabase.playlistDao().containsPlaylist(udi) > 0)
    }

    override fun contains(udi: String, id: Int): Flow<Boolean> = flow{

        val count = appDatabase.playlistDao().containsTrack(udi, id)
        emit(count > 0)
    }

    override suspend fun addToPlaylist(udi: String, track: Track) {
        appDatabase.trackDao().insertTrack(trackConvertor.map(track))
        appDatabase.playlistDao().addTrack(udi, track.trackId)
        update()
    }
}