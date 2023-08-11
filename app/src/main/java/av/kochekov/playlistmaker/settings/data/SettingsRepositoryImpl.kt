package av.kochekov.playlistmaker.settings.data

import android.content.Context
import av.kochekov.playlistmaker.settings.domain.SettingsRepository
import av.kochekov.playlistmaker.settings.domain.models.ThemeSettings

private const val DATA_KEY = "IS_NIGHT_MODE"

class SettingsRepositoryImpl(context: Context) : SettingsRepository {

//    private val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private val sharedPreferences = context.getSharedPreferences(context.applicationInfo.loadLabel(context.packageManager).toString(), Context.MODE_PRIVATE)

    override fun getThemeSettings(): ThemeSettings {
        return if (sharedPreferences.getBoolean(DATA_KEY, false)) ThemeSettings.DARK else ThemeSettings.LIGHT
    }

    override fun updateThemeSetting(themeSettings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(DATA_KEY, themeSettings == ThemeSettings.DARK).apply()
    }
}