package av.kochekov.playlistmaker.common.data.db.dao

import androidx.room.*
import av.kochekov.playlistmaker.common.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(data: TrackEntity)

    @Delete
    suspend fun deleteTrack(data: TrackEntity)

    @Query("DELETE FROM tracks WHERE id = :id")
    suspend fun deleteTrack(id: Int)

    @Query("SELECT * FROM tracks WHERE id = :id")
    suspend fun getTrack(id: Int): List<TrackEntity>

    @Query("SELECT * FROM tracks")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT id FROM tracks")
    suspend fun getTrackIds(): List<Int>


}