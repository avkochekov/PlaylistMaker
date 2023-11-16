package av.kochekov.playlistmaker.favorite.domain

import av.kochekov.playlistmaker.common.data.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackRepositoryObserver{
    fun update()
}
interface FavoriteTrackRepository {
    companion object{
        var observerList: MutableList<FavoriteTrackRepositoryObserver> = mutableListOf()
    }

    fun getTracks(): Flow<List<Track>>;

    fun getTrackIds(): Flow<List<Int>>

    fun containsTrack(track: Track): Flow<Boolean>

    fun containsTrack(trackId: Int): Flow<Boolean>

    suspend fun addTrack(track: Track)

    suspend fun removeTrack(track: Track)

    suspend fun removeTrack(trackId: Int)
    fun attach(observer: FavoriteTrackRepositoryObserver){
        observerList.add(observer)
    }

    fun update(){
        observerList.map {
            it.update()
        }
    }
}