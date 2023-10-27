package av.kochekov.playlistmaker.library.data.db

import androidx.room.*
import av.kochekov.playlistmaker.library.data.db.dao.TrackDao
import av.kochekov.playlistmaker.library.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao(): TrackDao
}
