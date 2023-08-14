package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.search.data.model.Track

interface SearchHistoryInteractor {
    fun add(track: Track)

    fun get() : List<Track>

    fun clear()
}