package av.kochekov.playlistmaker.common.di

import androidx.room.Room
import av.kochekov.playlistmaker.common.data.db.AppDatabase
import av.kochekov.playlistmaker.images.data.ImagesRepositoryImpl
import av.kochekov.playlistmaker.images.domain.ImagesRepository
import av.kochekov.playlistmaker.common.data.converters.PlaylistDbConvertor
import av.kochekov.playlistmaker.common.data.repository.PlaylistRepositoryImpl
import av.kochekov.playlistmaker.playlist_editor.domain.PlaylistInteractor
import av.kochekov.playlistmaker.common.domain.PlaylistRepository
import av.kochekov.playlistmaker.common.data.interactor.PlaylistInteractorImpl
import av.kochekov.playlistmaker.library.domain.playlists.PlaylistsInteractor
import av.kochekov.playlistmaker.library.domain.playlists.PlaylistsInteractorImpl
import av.kochekov.playlistmaker.library.presentation.playlists.PlaylistsViewModel
import av.kochekov.playlistmaker.playlist_editor.presentation.PlaylistEditorViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistsModule = module {
    single<ImagesRepository> {
        ImagesRepositoryImpl(get())
    }

    factory { PlaylistDbConvertor() }

    single<PlaylistsInteractor> {
        PlaylistsInteractorImpl(
            playlistRepository = get(),
        )
    }

    viewModel<PlaylistsViewModel> {
        PlaylistsViewModel(
            interactor = get()
        )
    }
}