package av.kochekov.playlistmaker.creator

import av.kochekov.playlistmaker.network_client.itunes.ITunesNetworkClient
import av.kochekov.playlistmaker.repository.track_list_repository.data.TrackListRepositoryImpl
import av.kochekov.playlistmaker.repository.track_list_repository.domain.TrackListInteractor
import av.kochekov.playlistmaker.repository.track_list_repository.domain.TrackListRepository
import av.kochekov.playlistmaker.repository.track_list_repository.data.TrackListInteractorImpl

object TrackListCreator {
    private fun getTrackListRepository(): TrackListRepository {
        return TrackListRepositoryImpl(ITunesNetworkClient())
    }

    fun provideTrackListInteractor(): TrackListInteractor {
        return TrackListInteractorImpl(getTrackListRepository())
    }
}