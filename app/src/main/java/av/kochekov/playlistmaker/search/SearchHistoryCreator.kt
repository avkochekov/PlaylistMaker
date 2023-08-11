package av.kochekov.playlistmaker.search

import android.content.SharedPreferences
import av.kochekov.playlistmaker.search.domain.SearchHistoryInteractor
import av.kochekov.playlistmaker.search.domain.SearchHistoryInteractorImpl
import av.kochekov.playlistmaker.search.domain.SearchHistoryRepository
import av.kochekov.playlistmaker.search.data.SearchHistoryRepositoryImpl

object SearchHistoryCreator {
    private fun getSearchHistoryRepository(sharedPreferences: SharedPreferences): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(sharedPreferences)
    }

    fun provideSearchHistoryInteractor(sharedPreferences: SharedPreferences): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(sharedPreferences))
    }
}