package av.kochekov.playlistmaker.common.data.db.dao

import androidx.room.*
import av.kochekov.playlistmaker.common.data.db.entity.PlaylistEntity
import av.kochekov.playlistmaker.common.data.db.relationships.PlaylistTracksRelation

@Dao
interface PlayListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(data: PlaylistEntity)

    @Update
    suspend fun updatePlaylist(data: PlaylistEntity)

    @Delete
    suspend fun removePlaylist(data: PlaylistEntity)

    @Query("SELECT * FROM playlists")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlists WHERE udi = :udi")
    suspend fun getPlaylist(udi: String): List<PlaylistEntity>

    @Transaction
    @Query("SELECT * FROM playlists")
    suspend fun getPlaylistsWithTracks(): List<PlaylistTracksRelation>

    @Transaction
    @Query("SELECT * FROM playlists WHERE udi = :udi")
    suspend fun getPlaylistWithTracks(udi: String): PlaylistTracksRelation

    @Query("INSERT INTO track_to_playlist (udi, id) VALUES (:playlistUdi, :trackId)")
    suspend fun addTrack(playlistUdi: String, trackId: Int)

    @Query("SELECT id FROM track_to_playlist")
    suspend fun getTrackIds(): List<Int>

    @Query("SELECT COUNT(*) FROM track_to_playlist WHERE udi = :udi")
    suspend fun containsPlaylist(udi: String): Int

    @Query("SELECT COUNT(*) FROM track_to_playlist WHERE udi = :udi AND id = :id")
    suspend fun containsTrack(udi: String, id: Int): Int
}