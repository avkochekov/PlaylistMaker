package av.kochekov.playlistmaker.settings

import android.content.Context
import av.kochekov.playlistmaker.settings.data.SettingsRepositoryImpl
import av.kochekov.playlistmaker.settings.domain.SettingsInteractor
import av.kochekov.playlistmaker.settings.domain.SettingsInteractorImpl
import av.kochekov.playlistmaker.settings.domain.SettingsRepository

object SettingsCreator {

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }
}