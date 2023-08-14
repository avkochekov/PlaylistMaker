package av.kochekov.playlistmaker.settings.di

import av.kochekov.playlistmaker.search.presentation.SearchViewModel
import av.kochekov.playlistmaker.settings.data.ExternalNavigator
import av.kochekov.playlistmaker.settings.data.SettingsRepositoryImpl
import av.kochekov.playlistmaker.settings.domain.*
import av.kochekov.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {

    single<ExternalNavigator> {
        ExternalNavigator(
            context = get()
        )
    }

    single<SharingInteractor> {
        SharingInteractorImpl(
            context = get()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            context = get()
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