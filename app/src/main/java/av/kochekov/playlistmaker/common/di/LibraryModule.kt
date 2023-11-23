package av.kochekov.playlistmaker.player.di

import androidx.room.Room
import av.kochekov.playlistmaker.common.data.repository.TrackRepositoryImpl
import av.kochekov.playlistmaker.favorite.data.converters.TrackDbConvertor
import av.kochekov.playlistmaker.common.data.db.AppDatabase
import av.kochekov.playlistmaker.common.domain.interactor.TrackInteractorImpl
import av.kochekov.playlistmaker.favorite_tracks.domain.TrackRepository
import av.kochekov.playlistmaker.favorite_tracks.domain.TrackInteractor
import av.kochekov.playlistmaker.library.domain.favorite.FavoriteTracksInteractor
import av.kochekov.playlistmaker.library.domain.favorite.FavoriteTracksInteractorImpl
import av.kochekov.playlistmaker.library.presentation.favorite.FavoriteTracksViewModel
import av.kochekov.playlistmaker.library.presentation.playlists.PlaylistsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    factory { TrackDbConvertor() }

    single<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    single<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    single<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get())
    }

    viewModel<FavoriteTracksViewModel> {
        FavoriteTracksViewModel(
            interactor = get()
        )
    }

    viewModel<PlaylistsViewModel> {
        PlaylistsViewModel(
            interactor = get()
        )
    }
}