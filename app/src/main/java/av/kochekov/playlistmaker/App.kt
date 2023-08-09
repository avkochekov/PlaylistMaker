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

    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()

        settingsInteractor = RepositoryCreator.provideSettingsInteractor(applicationContext)
        setIsNightMode(getIsNightMode(), true)
    }

    fun getIsNightMode(): Boolean{
        return settingsInteractor.getThemeSettings().equals(ThemeSettings.DARK)
    }

    fun setIsNightMode(state: Boolean, force: Boolean = false){
        if (state == getIsNightMode() && !force)
            return

        settingsInteractor.updateThemeSetting(if (state) ThemeSettings.DARK else ThemeSettings.LIGHT)

        AppCompatDelegate.setDefaultNightMode(
            if (state) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}