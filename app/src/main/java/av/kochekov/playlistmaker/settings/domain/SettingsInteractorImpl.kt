package av.kochekov.playlistmaker.settings.domain

import av.kochekov.playlistmaker.settings.domain.models.ThemeSettings

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return repository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        repository.updateThemeSetting(settings)
    }
}