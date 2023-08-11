package av.kochekov.playlistmaker.search.data

import android.content.SharedPreferences
import av.kochekov.playlistmaker.search.domain.SearchHistoryRepository
import av.kochekov.playlistmaker.search.data.model.Track
import com.google.gson.Gson

private const val SHARED_PREF_NAME = "TRACK_LIST_HISTORY"
private const val DATA_KEY = "TRACK_LIST"

class SearchHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SearchHistoryRepository {

    override fun setTrackList(list: List<Track>) {
        sharedPreferences.edit().putString(DATA_KEY, Gson().toJson(list)).apply()
    }

    override fun getTrackList(): List<Track> {
        return sharedPreferences.getString(DATA_KEY, null)
            ?.let {
                return Gson().fromJson(it, Array<Track>::class.java).toList()
            }
            ?: mutableListOf()
    }

    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}