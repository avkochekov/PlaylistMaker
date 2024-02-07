package av.kochekov.playlistmaker.playlist_view.domain

import av.kochekov.playlistmaker.common.data.ExternalNavigator

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun sharePlaylist(data: String) {
        externalNavigator.shareText(data)
    }
}