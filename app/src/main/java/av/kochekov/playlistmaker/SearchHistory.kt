package av.kochekov.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import com.google.gson.Gson

object SearchHistory{
    const val DATA_KEY = "TRACK_LIST_HISTORY"
    const val MAX_LIST_SIZE = 10

    lateinit var pref: SharedPreferences

    fun add(track: Track){
        var trackList = get().toMutableList()
        trackList.remove(track)
        if (trackList.size == MAX_LIST_SIZE)
            trackList.removeFirst()
        trackList.add(track)
        pref.edit()
            .putString(DATA_KEY, Gson().toJson(trackList))
            .apply()
    }

    fun get(isReversed: Boolean = false): List<Track> {
        return pref.getString(DATA_KEY, null)
            ?.let {
                var list = Gson().fromJson(it, Array<Track>::class.java).toList()
                return if (isReversed) list.reversed() else list
            }
            ?: mutableListOf()
    }

    fun clear(){
        pref.edit()
            .clear()
            .apply()
    }
}

//class SearchHistory(private val pref: SharedPreferences) {
//    companion object{
//        const val DATA_KEY = "TRACK_LIST_HISTORY"
//        const val MAX_LIST_SIZE = 10
//    }
//
//    fun add(track: Track){
//        var trackList = get().toMutableList()
//        trackList.remove(track)
//        if (trackList.size == MAX_LIST_SIZE)
//            trackList.removeFirst()
//        trackList.add(track)
//        pref.edit()
//            .putString(DATA_KEY, Gson().toJson(trackList))
//            .apply()
//    }
//
//    fun get(isReversed: Boolean = false): List<Track> {
//        return pref.getString(DATA_KEY, null)
//            ?.let {
//                var list = Gson().fromJson(it, Array<Track>::class.java).toList()
//                return if (isReversed) list.reversed() else list
//            }
//            ?: mutableListOf()
//    }
//
//    fun clear(){
//        pref.edit()
//            .clear()
//            .apply()
//    }
//}