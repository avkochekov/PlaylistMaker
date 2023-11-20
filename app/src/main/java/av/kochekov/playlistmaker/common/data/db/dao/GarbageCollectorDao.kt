package av.kochekov.playlistmaker.common.data.db.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface GarbageCollectorDao {
    @Query("DELETE FROM tracks\n" +
            "      WHERE id IN (\n" +
            "    SELECT tracks.id\n" +
            "      FROM tracks\n" +
            "           LEFT JOIN\n" +
            "           favorite_tracks ON favorite_tracks.id = tracks.id\n" +
            "           LEFT JOIN\n" +
            "           track_to_playlist ON track_to_playlist.id = tracks.id\n" +
            "     WHERE favorite_tracks.id IS NULL AND \n" +
            "           track_to_playlist.id IS NULL\n" +
            ");\n")
    suspend fun clearTracks()
}