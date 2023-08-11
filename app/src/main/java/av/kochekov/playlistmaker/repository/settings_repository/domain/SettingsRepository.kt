package av.kochekov.playlistmaker.repository.settings_repository.domain

import av.kochekov.playlistmaker.settings.domain.models.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}