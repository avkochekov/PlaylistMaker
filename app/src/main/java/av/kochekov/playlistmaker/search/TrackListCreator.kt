package av.kochekov.playlistmaker.search

import av.kochekov.playlistmaker.search.data.network_client.itunes.ITunesNetworkClient
import av.kochekov.playlistmaker.search.data.TrackListRepositoryImpl
import av.kochekov.playlistmaker.search.data.network_client.NetworkClient
import av.kochekov.playlistmaker.search.domain.TrackListInteractor
import av.kochekov.playlistmaker.search.domain.TrackListRepository
import av.kochekov.playlistmaker.search.domain.TrackListInteractorImpl

object TrackListCreator {
    private fun getTrackListNetworkClient(): NetworkClient {
        return ITunesNetworkClient()
    }

    private fun getTrackListRepository(): TrackListRepository {
        return TrackListRepositoryImpl(getTrackListNetworkClient())
    }

    fun provideTrackListInteractor(): TrackListInteractor {
        return TrackListInteractorImpl(getTrackListRepository())
    }
}