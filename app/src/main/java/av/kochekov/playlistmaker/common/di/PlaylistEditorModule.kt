package av.kochekov.playlistmaker.common.di

import androidx.room.Room
import av.kochekov.playlistmaker.common.data.db.AppDatabase
import av.kochekov.playlistmaker.images.data.ImagesRepositoryImpl
import av.kochekov.playlistmaker.images.domain.ImagesRepository
import av.kochekov.playlistmaker.common.data.converters.PlaylistDbConvertor
import av.kochekov.playlistmaker.common.data.repository.PlaylistRepositoryImpl
import av.kochekov.playlistmaker.playlist_editor.domain.PlaylistInteractor
import av.kochekov.playlistmaker.common.domain.PlaylistRepository
import av.kochekov.playlistmaker.common.domain.interactor.PlaylistInteractorImpl
import av.kochekov.playlistmaker.playlist_editor.presentation.PlaylistEditorViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistEditorModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<ImagesRepository> {
        ImagesRepositoryImpl(get())
    }

    factory { PlaylistDbConvertor() }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            appDatabase = get(),
            playlistConvertor = get(),
            trackConvertor = get()
        )
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(
            playlistRepository = get(),
            imagesRepository = get(),
        )
    }

    viewModel<PlaylistEditorViewModel> {
        PlaylistEditorViewModel(
            interactor = get()
        )
    }
}