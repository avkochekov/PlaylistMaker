package av.kochekov.playlistmaker.data.search

import android.content.Context
import av.kochekov.playlistmaker.domain.search.api.SearchHistoryRepositoryInterface
import av.kochekov.playlistmaker.domain.search.model.Track
import com.google.gson.Gson

private const val SHARED_PREF_NAME = "TRACK_LIST_HISTORY"
private const val DATA_KEY = "TRACK_LIST"

class SearchHistoryRepositoryImpl(context: Context) : SearchHistoryRepositoryInterface {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

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