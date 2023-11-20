package av.kochekov.playlistmaker.playlist_view.domain

import android.content.Context
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.common.data.ExternalNavigator
import av.kochekov.playlistmaker.playlist_view.domain.models.PlaylistModel

class SharingInteractorImpl(
    private val context: Context,
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun sharePlaylist(data: PlaylistModel) {
        var message = String()
        message += "${data.name}\n"
        message += "${data.description}\n"
        message += context.resources.getQuantityString(
            R.plurals.tracks,
            data.tracks.size,
            data.tracks.size
        ) + "\n"
        var index = 1
        for (item in data.tracks)
            message += "${index++}. ${item.artistName} - ${item.trackName} (${item.duration})\n"
        externalNavigator.shareText(message)
    }
}