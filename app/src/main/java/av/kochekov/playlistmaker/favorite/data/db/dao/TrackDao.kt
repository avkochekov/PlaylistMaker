package av.kochekov.playlistmaker.favorite.data.db.dao

import androidx.room.*
import av.kochekov.playlistmaker.favorite.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("DELETE FROM favorite_tracks WHERE id = :id")
    suspend fun deleteTrack(id: Int)

    @Query("SELECT * FROM favorite_tracks")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT id FROM favorite_tracks")
    suspend fun getTrackIds(): List<Int>
}