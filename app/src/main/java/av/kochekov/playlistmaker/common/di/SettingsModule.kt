package av.kochekov.playlistmaker.settings.di

import android.content.Context
import android.content.SharedPreferences
import av.kochekov.playlistmaker.common.data.ExternalNavigator
import av.kochekov.playlistmaker.settings.data.SettingsRepositoryImpl
import av.kochekov.playlistmaker.settings.domain.*
import av.kochekov.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val SHARED_PREFERENCES_NAME = "local_storage"

val settingsModule = module {

    single<ExternalNavigator> {
        ExternalNavigator(
            context = get()
        )
    }

    single<SharedPreferences> {
        androidContext()
            .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    single<SharingInteractor> {
        SharingInteractorImpl(
            context = get(),
            externalNavigator = get()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            sharedPreferences = get()
        )
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(
            repository = get()
        )
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            sharingInteractor = get(),
            settingsInteractor = get(),
        )
    }
}