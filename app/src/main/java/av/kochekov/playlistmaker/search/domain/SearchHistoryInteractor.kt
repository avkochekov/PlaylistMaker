package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.common.domain.TrackModel

interface SearchHistoryInteractor {
    fun add(track: TrackModel)

    fun get(): List<TrackModel>

    fun clear()
}