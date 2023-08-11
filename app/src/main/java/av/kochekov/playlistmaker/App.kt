package av.kochekov.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import av.kochekov.playlistmaker.creator.RepositoryCreator
import av.kochekov.playlistmaker.repository.settings_repository.domain.SettingsInteractor
import av.kochekov.playlistmaker.settings.domain.models.ThemeSettings

class App : Application() {
    companion object {
        var preferences: SharedPreferences? = null
        private const val DATA_KEY = "IS_NIGHT_MODE"
    }

    override fun onCreate() {
        super.onCreate()
        preferences = getSharedPreferences(applicationInfo.loadLabel(packageManager).toString(), MODE_PRIVATE)
        preferences?.run {
            var listener =
                SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                    if (key == DATA_KEY) {
                        setIsNightMode(getIsNightMode())
                    }
                }
            registerOnSharedPreferenceChangeListener(listener)
        }
        setIsNightMode(getIsNightMode(), true)
    }

    fun getIsNightMode(): Boolean{
        return preferences?.getBoolean(DATA_KEY, false) ?: false
    }
    fun setIsNightMode(state: Boolean, force: Boolean = false){
        if (state == getIsNightMode() && !force)
            return
        preferences?.run{
            edit().putBoolean(DATA_KEY, state).commit()
        }
        AppCompatDelegate.setDefaultNightMode(
            if (state) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}