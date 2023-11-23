package av.kochekov.playlistmaker.playlist_view.presentation.utils

import av.kochekov.playlistmaker.playlist_view.domain.models.TrackModel

object Formatter {
    fun getTotalTime(data: List<TrackModel>) : Int {
        var total = 0
        for (item in data)
            item.trackTimeMillis?.let { total += it }
        return total / 1000 / 60    // Conversion from ms to min
    }
}