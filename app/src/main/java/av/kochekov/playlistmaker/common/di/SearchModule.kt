package av.kochekov.playlistmaker.search.di

import av.kochekov.playlistmaker.search.data.SearchHistoryRepositoryImpl
import av.kochekov.playlistmaker.search.data.TrackListRepositoryImpl
import av.kochekov.playlistmaker.search.data.network_client.NetworkClient
import av.kochekov.playlistmaker.search.data.network_client.itunes.ITunesApi
import av.kochekov.playlistmaker.search.data.network_client.itunes.ITunesNetworkClient
import av.kochekov.playlistmaker.search.domain.*
import av.kochekov.playlistmaker.search.presentation.SearchViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchModule = module {

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl(ITunesApi.apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

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

    single<NetworkClient> {
        ITunesNetworkClient(
            service = get(),
            androidContext()
        )
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

    viewModel<SearchViewModel> {
        SearchViewModel(
            trackListInteractor = get(),
            searchHistoryInteractor = get()
        )
    }
}