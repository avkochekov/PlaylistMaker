package av.kochekov.playlistmaker.player.di

import androidx.room.Room
import av.kochekov.playlistmaker.common.data.repository.FavoriteTrackRepositoryImpl
import av.kochekov.playlistmaker.favorite.data.converters.TrackDbConvertor
import av.kochekov.playlistmaker.common.data.db.AppDatabase
import av.kochekov.playlistmaker.common.data.interactor.FavoriteTrackInteractorImpl
import av.kochekov.playlistmaker.favorite.domain.FavoriteTrackRepository
import av.kochekov.playlistmaker.favorite_tracks.domain.FavoriteTrackInteractor
import av.kochekov.playlistmaker.library.presentation.favorite.FavoriteTracksViewModel
import av.kochekov.playlistmaker.library.presentation.playlists.PlayListsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    factory { TrackDbConvertor() }

    single<FavoriteTrackRepository> {
        FavoriteTrackRepositoryImpl(get(), get())
    }

    single<FavoriteTrackInteractor> {
        FavoriteTrackInteractorImpl(get())
    }

    viewModel<FavoriteTracksViewModel> {
        FavoriteTracksViewModel(
            interactor = get()
        )
    }

    viewModel<PlayListsViewModel> {
        PlayListsViewModel(
            interactor = get()
        )
    }
}