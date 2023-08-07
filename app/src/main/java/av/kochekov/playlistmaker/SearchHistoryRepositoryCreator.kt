package av.kochekov.playlistmaker

import android.content.Context
import av.kochekov.playlistmaker.data.search.SearchHistoryRepositoryImpl
import av.kochekov.playlistmaker.domain.search.api.SearchHistoryInteractor
import av.kochekov.playlistmaker.domain.search.api.SearchHistoryRepositoryInterface
import av.kochekov.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl

class SearchHistoryRepositoryCreator {
//    applicationInfo.loadLabel(packageManager).toString()

private fun getSearchHistoryRepository(context: Context): SearchHistoryRepositoryInterface {
    return SearchHistoryRepositoryImpl(context)
}

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }
}