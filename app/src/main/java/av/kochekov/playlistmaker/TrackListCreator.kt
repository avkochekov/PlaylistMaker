package av.kochekov.playlistmaker

import av.kochekov.playlistmaker.data.search.ITunesNetworkClient
import av.kochekov.playlistmaker.data.search.TrackListRepositoryImpl
import av.kochekov.playlistmaker.domain.search.api.TrackListInteractor
import av.kochekov.playlistmaker.domain.search.api.TrackListRepositoryInterface
import av.kochekov.playlistmaker.domain.search.impl.TrackListInteractorImpl

object TrackListCreator {
    private fun getTrackListRepository(): TrackListRepositoryInterface {
        return TrackListRepositoryImpl(ITunesNetworkClient())
    }

    fun provideTrackListInteractor(): TrackListInteractor {
        return TrackListInteractorImpl(getTrackListRepository())
    }
}