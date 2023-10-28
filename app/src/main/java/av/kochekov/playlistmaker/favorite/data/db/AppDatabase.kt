package av.kochekov.playlistmaker.favorite.data.db

import androidx.room.*
import av.kochekov.playlistmaker.favorite.data.db.dao.TrackDao
import av.kochekov.playlistmaker.favorite.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao(): TrackDao
}
