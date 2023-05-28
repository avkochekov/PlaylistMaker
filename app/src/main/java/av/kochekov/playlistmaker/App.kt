package av.kochekov.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    companion object {
        lateinit var preferences: SharedPreferences
        private const val DATA_KEY = "IS_NIGHT_MODE"
    }

    override fun onCreate() {
        super.onCreate()
        preferences = getSharedPreferences(applicationInfo.loadLabel(packageManager).toString(), MODE_PRIVATE)
        preferences.apply {
            var listener =
                SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                    if (key == DATA_KEY) {
                        setIsNightMode(getIsNightMode())
                    }
                }
            registerOnSharedPreferenceChangeListener(listener)
        }
        setIsNightMode(getIsNightMode(), true)
    }

    fun getIsNightMode(): Boolean{
        return preferences.getBoolean(DATA_KEY, false)
    }

    fun setIsNightMode(state: Boolean, force: Boolean = false){
        if (state == getIsNightMode() && !force)
            return
        preferences.edit().putBoolean(DATA_KEY, state).commit()
        AppCompatDelegate.setDefaultNightMode(
            if (state) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}