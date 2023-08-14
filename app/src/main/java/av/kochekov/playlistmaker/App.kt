package av.kochekov.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import av.kochekov.playlistmaker.player.di.playerModule
import av.kochekov.playlistmaker.search.di.searchModule
import av.kochekov.playlistmaker.settings.di.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    companion object {
        var preferences: SharedPreferences? = null
        private const val DATA_KEY = "IS_NIGHT_MODE"
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(settingsModule, searchModule, playerModule)
        }

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