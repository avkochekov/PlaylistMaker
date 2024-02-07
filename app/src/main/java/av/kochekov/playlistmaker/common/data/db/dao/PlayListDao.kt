package av.kochekov.playlistmaker.common.data.db.dao

import androidx.room.*
import av.kochekov.playlistmaker.common.data.db.entity.PlaylistEntity
import av.kochekov.playlistmaker.common.data.db.relationships.PlaylistTracksRelation
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.UUID

@Dao
interface PlayListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(data: PlaylistEntity)

    @Update
    suspend fun updatePlaylist(data: PlaylistEntity)

    @Delete
    suspend fun removePlaylist(data: PlaylistEntity)

    @Query("DELETE FROM playlists WHERE udi = :uuid")
    suspend fun removePlaylistInfo(uuid: String)
    @Query("DELETE FROM track_to_playlist WHERE udi = :uuid")
    suspend fun removePlaylistTracks(uuid: String)

    @Transaction
    suspend fun removePlaylist(uuid: String){
        removePlaylistInfo(uuid)
        removePlaylistTracks(uuid)
    }

    @Query("SELECT * FROM playlists")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlists WHERE udi = :uuid")
    suspend fun getPlaylist(uuid: String): List<PlaylistEntity>

    @Transaction
    @Query("SELECT * FROM playlists")
    suspend fun getPlaylistsWithTracks(): List<PlaylistTracksRelation>

    @Transaction
    @Query("SELECT * FROM playlists WHERE udi = :uuid")
    suspend fun getPlaylistWithTracks(uuid: String): PlaylistTracksRelation?

    @Query("INSERT INTO track_to_playlist (udi, id) VALUES (:playlistUUID, :trackId)")
    suspend fun addTrack(playlistUUID: String, trackId: Int)

    @Query("DELETE FROM track_to_playlist WHERE udi = :playlistUUID AND id = :trackId")
    suspend fun removeTrack(playlistUUID: String, trackId: Int)

    @Query("SELECT COUNT(*) FROM track_to_playlist WHERE udi = :uuid")
    suspend fun containsPlaylist(uuid: String): Int

    @Query("SELECT COUNT(*) FROM track_to_playlist WHERE udi = :uuid AND id = :id")
    suspend fun containsTrack(uuid: String, id: Int): Int

    @Query("SELECT COUNT(*) FROM track_to_playlist WHERE id = :id")
    suspend fun containsTrack(id: Int): Int
}