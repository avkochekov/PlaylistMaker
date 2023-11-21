package av.kochekov.playlistmaker.common.di

import av.kochekov.playlistmaker.images.data.ImagesRepositoryImpl
import av.kochekov.playlistmaker.images.domain.ImagesRepository
import av.kochekov.playlistmaker.playlist_view.domain.PlaylistViewInteractor
import av.kochekov.playlistmaker.playlist_view.domain.PlaylistViewInteractorImpl
import av.kochekov.playlistmaker.playlist_view.domain.SharingInteractor
import av.kochekov.playlistmaker.playlist_view.domain.SharingInteractorImpl
import av.kochekov.playlistmaker.playlist_view.presentation.PlaylistViewViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistViewModule = module {
    single<ImagesRepository> {
        ImagesRepositoryImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(
            context = androidContext(),
            externalNavigator = get()
        )
    }

    single<PlaylistViewInteractor> {
        PlaylistViewInteractorImpl(
            playlistRepository = get()
        )
    }

    viewModel<PlaylistViewViewModel> {
        PlaylistViewViewModel(
            playlistInteractor = get(),
            sharingInteractor = get()
        )
    }
}