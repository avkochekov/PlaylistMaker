package av.kochekov.playlistmaker.favorite.domain

import av.kochekov.playlistmaker.search.data.model.Track
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

    suspend fun addTrack(track: Track)

    suspend fun removeTrack(track: Track)
    fun attach(observer: FavoriteTrackRepositoryObserver){
        observerList.add(observer)
    }

    fun update(){
        observerList.map {
            it.update()
        }
    }
}