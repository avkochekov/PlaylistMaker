package av.kochekov.playlistmaker.common.data.repository

import av.kochekov.playlistmaker.common.data.db.AppDatabase
import av.kochekov.playlistmaker.favorite.data.converters.TrackDbConvertor
import av.kochekov.playlistmaker.common.data.converters.PlaylistDbConvertor
import av.kochekov.playlistmaker.common.domain.PlaylistRepository
import av.kochekov.playlistmaker.common.data.models.Playlist
import av.kochekov.playlistmaker.common.data.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistConvertor: PlaylistDbConvertor,
    private val trackConvertor: TrackDbConvertor,
) : PlaylistRepository {

    override fun addPlaylist(playlist: Playlist): Flow<Playlist?> = flow {
        appDatabase.playlistDao().insertPlaylist(playlistConvertor.map(playlist))
        appDatabase.playlistDao().getPlaylistWithTracks(playlist.uuid)
            ?.let { data -> emit(playlistConvertor.map(data)) }
    }

    override fun updatePlaylist(playlist: Playlist): Flow<Playlist?> = flow {
        appDatabase.playlistDao().updatePlaylist(playlistConvertor.map(playlist))
        appDatabase.playlistDao().getPlaylistWithTracks(playlist.uuid)
            ?.let { data -> emit(playlistConvertor.map(data)) }

    }

    override suspend fun removePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().removePlaylist(playlistConvertor.map(playlist))
        appDatabase.garbageCollectorDao().clearTracks()
    }

    override suspend fun removePlaylist(uuid: String) {
        appDatabase.playlistDao().removePlaylist(uuid)
        appDatabase.garbageCollectorDao().clearTracks()
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val list = appDatabase.playlistDao().getPlaylistsWithTracks()
        emit(list.map { model -> playlistConvertor.map(model) })
    }

    override fun getPlaylist(uuid: String): Flow<Playlist?> = flow {
        appDatabase.playlistDao().getPlaylistWithTracks(uuid)
            ?.let { data -> emit(playlistConvertor.map(data)) }
    }

    override fun containsPlaylists(uuid: String): Flow<Boolean> = flow {
        emit(appDatabase.playlistDao().containsPlaylist(uuid) > 0)
    }

    override fun containsTrack(uuid: String, id: Int): Flow<Boolean> = flow {

        val count = appDatabase.playlistDao().containsTrack(uuid, id)
        emit(count > 0)
    }

    override fun containsTrack(id: Int): Flow<Boolean> = flow {
        val count = appDatabase.playlistDao().containsTrack(id)
        emit(count > 0)
    }

    override suspend fun addTrack(udi: String, track: Track) {
        appDatabase.trackDao().insertTrack(trackConvertor.map(track))
        appDatabase.playlistDao().addTrack(udi, track.trackId)
    }

    override suspend fun removeTrack(udi: String, id: Int) {
        appDatabase.playlistDao().removeTrack(udi, id)
        appDatabase.garbageCollectorDao().clearTracks()
    }
}