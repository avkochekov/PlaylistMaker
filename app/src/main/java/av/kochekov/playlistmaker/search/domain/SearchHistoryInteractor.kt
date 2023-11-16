package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.common.data.models.Track

interface SearchHistoryInteractor {
    fun add(track: Track)

    fun get() : List<Track>

    fun clear()
}