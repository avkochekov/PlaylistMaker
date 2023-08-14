package av.kochekov.playlistmaker.search.di

import android.content.Context
import av.kochekov.playlistmaker.search.data.SearchHistoryRepositoryImpl
import av.kochekov.playlistmaker.search.data.TrackListRepositoryImpl
import av.kochekov.playlistmaker.search.data.network_client.NetworkClient
import av.kochekov.playlistmaker.search.data.network_client.itunes.ITunesNetworkClient
import av.kochekov.playlistmaker.search.domain.*
import av.kochekov.playlistmaker.search.presentation.SearchViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(
            sharedPreferences = get()
        )
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(
            repository = get()
        )
    }

    single {
        androidContext()
            .getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    single<NetworkClient> {
        ITunesNetworkClient()
    }

    single<TrackListRepository> {
        TrackListRepositoryImpl(
            networkClient = get()
        )
    }

    single<TrackListInteractor> {
        TrackListInteractorImpl(
            repository = get()
        )
    }

    factory { Gson() }

    viewModel<SearchViewModel>{
        SearchViewModel(
            trackListInteractor = get(),
            searchHistoryInteractor = get()
        )
    }
}