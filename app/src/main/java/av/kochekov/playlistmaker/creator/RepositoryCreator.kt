package av.kochekov.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import av.kochekov.playlistmaker.search.data.network_client.NetworkClient
import av.kochekov.playlistmaker.search.data.network_client.itunes.ITunesNetworkClient
import av.kochekov.playlistmaker.search.domain.SearchHistoryInteractor
import av.kochekov.playlistmaker.search.domain.SearchHistoryInteractorImpl
import av.kochekov.playlistmaker.search.domain.SearchHistoryRepository
import av.kochekov.playlistmaker.search.data.SearchHistoryRepositoryImpl
import av.kochekov.playlistmaker.settings.domain.SettingsInteractorImpl
import av.kochekov.playlistmaker.settings.domain.SettingsRepository
import av.kochekov.playlistmaker.settings.data.SettingsRepositoryImpl
import av.kochekov.playlistmaker.search.domain.TrackListInteractorImpl
import av.kochekov.playlistmaker.search.data.TrackListRepositoryImpl
import av.kochekov.playlistmaker.search.domain.TrackListInteractor
import av.kochekov.playlistmaker.search.domain.TrackListRepository
import av.kochekov.playlistmaker.settings.domain.SettingsInteractor

object RepositoryCreator {
    private fun getSearchHistoryRepository(sharedPreferences: SharedPreferences): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(sharedPreferences)
    }

    fun provideSearchHistoryInteractor(sharedPreferences: SharedPreferences): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(sharedPreferences))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private fun getTrackListNetworkClient(): NetworkClient {
        return ITunesNetworkClient()
    }

    private fun getTrackListRepository(): TrackListRepository {
        return TrackListRepositoryImpl(getTrackListNetworkClient())
    }
    fun provideTrackListInteractor(context: Context): TrackListInteractor {
        return TrackListInteractorImpl(getTrackListRepository())
    }
}