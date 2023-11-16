package av.kochekov.playlistmaker.common.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import av.kochekov.playlistmaker.common.data.db.relationships.FavoriteTrackRelation

@Dao
interface FavoriteTracksDao {
    @Query("SELECT * FROM favorite_tracks")
    suspend fun getTracks(): List<FavoriteTrackRelation>

    @Query("SELECT id FROM favorite_tracks")
    suspend fun getTrackIds(): List<Int>

    @Query("INSERT INTO favorite_tracks VALUES (:id)")
    suspend fun insertTrack(id: Int)

    @Query("DELETE FROM favorite_tracks WHERE id = :id")
    suspend fun deleteTrack(id: Int)

    @Query("SELECT * FROM favorite_tracks WHERE id = :id")
    suspend fun contains(id: Int): List<FavoriteTrackRelation>
}