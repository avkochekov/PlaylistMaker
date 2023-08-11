package av.kochekov.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import av.kochekov.playlistmaker.network_client.NetworkClient
import av.kochekov.playlistmaker.network_client.itunes.ITunesNetworkClient
import av.kochekov.playlistmaker.repository.search_repository.domain.SearchHistoryInteractor
import av.kochekov.playlistmaker.repository.search_repository.data.SearchHistoryInteractorImpl
import av.kochekov.playlistmaker.repository.search_repository.domain.SearchHistoryRepository
import av.kochekov.playlistmaker.repository.search_repository.data.SearchHistoryRepositoryImpl
import av.kochekov.playlistmaker.repository.settings_repository.domain.SettingsInteractor
import av.kochekov.playlistmaker.repository.settings_repository.data.SettingsInteractorImpl
import av.kochekov.playlistmaker.repository.settings_repository.domain.SettingsRepository
import av.kochekov.playlistmaker.repository.settings_repository.data.SettingsRepositoryImpl
import av.kochekov.playlistmaker.repository.track_list_repository.data.TrackListInteractorImpl
import av.kochekov.playlistmaker.repository.track_list_repository.data.TrackListRepositoryImpl
import av.kochekov.playlistmaker.repository.track_list_repository.domain.TrackListInteractor
import av.kochekov.playlistmaker.repository.track_list_repository.domain.TrackListRepository

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