package av.kochekov.playlistmaker.domain.search.api

import av.kochekov.playlistmaker.domain.search.model.Track

interface SearchHistoryInteractor {
    fun add(track: Track)

    fun get() : List<Track>

    fun clear()
}