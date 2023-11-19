package av.kochekov.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import av.kochekov.playlistmaker.player.di.libraryModule
import av.kochekov.playlistmaker.player.di.playerModule
import av.kochekov.playlistmaker.common.di.playlistModule
import av.kochekov.playlistmaker.search.di.searchModule
import av.kochekov.playlistmaker.settings.di.settingsModule
import av.kochekov.playlistmaker.settings.domain.SettingsRepository
import av.kochekov.playlistmaker.settings.domain.models.ThemeSettings
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    private val preferences: SettingsRepository by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(settingsModule, searchModule, playerModule, libraryModule, playlistModule)
        }

        setIsNightMode(getIsNightMode(), true)
    }

    private fun getIsNightMode(): Boolean{
        return preferences.getThemeSettings()?.equals(ThemeSettings.DARK) ?: false
    }
    private fun setIsNightMode(state: Boolean, force: Boolean = false){
        if (state == getIsNightMode() && !force)
            return
        preferences.updateThemeSetting(if (state) ThemeSettings.DARK else ThemeSettings.LIGHT)
        AppCompatDelegate.setDefaultNightMode(
            if (state) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}