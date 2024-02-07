package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.search.domain.model.TrackModel

interface SearchHistoryInteractor {
    fun add(track: TrackModel)

    fun get(): List<TrackModel>

    fun clear()
}