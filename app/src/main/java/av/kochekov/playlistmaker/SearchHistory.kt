package av.kochekov.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(private val pref: SharedPreferences) {
    companion object{
        const val DATA_KEY = "TRACK_LIST_HISTORY"
    }

    fun add(track: Track){
        var trackList = get().toMutableList()
        trackList.remove(track)
        if (trackList.size == 10)
            trackList.removeFirst()
        trackList.add(track)
        pref.edit()
            .putString(DATA_KEY, Gson().toJson(trackList))
            .apply()
    }

    fun get(): List<Track> {
        return pref.getString(DATA_KEY, null)
            ?.let {
                return Gson().fromJson(it, Array<Track>::class.java).toList()
            }
            ?: mutableListOf()
    }

    fun clear(){
        pref.edit()
            .clear()
            .apply()
    }
}