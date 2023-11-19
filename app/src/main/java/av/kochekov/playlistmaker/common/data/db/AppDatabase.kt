package av.kochekov.playlistmaker.common.data.db

import androidx.room.*
import av.kochekov.playlistmaker.common.data.db.dao.FavoriteTracksDao
import av.kochekov.playlistmaker.common.data.db.dao.PlayListDao
import av.kochekov.playlistmaker.common.data.db.dao.TrackDao
import av.kochekov.playlistmaker.common.data.db.entity.FavoriteTrackEntity
import av.kochekov.playlistmaker.common.data.db.entity.PlaylistEntity
import av.kochekov.playlistmaker.common.data.db.entity.TrackEntity
import av.kochekov.playlistmaker.common.data.db.entity.TrackToPlaylistEntity

@Database(
    version = 4,
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        FavoriteTrackEntity::class,
        TrackToPlaylistEntity::class,
    ]
)
abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao(): TrackDao
    abstract fun favoriteTracks(): FavoriteTracksDao
    abstract fun playlistDao(): PlayListDao
}
