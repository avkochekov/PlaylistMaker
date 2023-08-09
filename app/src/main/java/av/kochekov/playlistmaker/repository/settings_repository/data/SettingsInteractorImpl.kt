package av.kochekov.playlistmaker.repository.settings_repository.data

import av.kochekov.playlistmaker.repository.settings_repository.domain.SettingsInteractor
import av.kochekov.playlistmaker.repository.settings_repository.domain.SettingsRepository
import av.kochekov.playlistmaker.settings.domain.models.ThemeSettings

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return repository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        repository.updateThemeSetting(settings)
    }
}