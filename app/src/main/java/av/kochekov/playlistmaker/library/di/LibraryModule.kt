package av.kochekov.playlistmaker.player.di

import androidx.room.Room
import av.kochekov.playlistmaker.favorite.data.FavoriteTrackRepositoryImpl
import av.kochekov.playlistmaker.favorite.data.converters.TrackDbConvertor
import av.kochekov.playlistmaker.favorite.data.db.AppDatabase
import av.kochekov.playlistmaker.favorite.domain.FavoriteTrackInteractorImpl
import av.kochekov.playlistmaker.favorite.domain.FavoriteTrackRepository
import av.kochekov.playlistmaker.favorite.domain.db.FavoriteTrackInteractor
import av.kochekov.playlistmaker.library.presentation.favorite.FavoriteTracksViewModel
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
            interactor = get())
    }
}