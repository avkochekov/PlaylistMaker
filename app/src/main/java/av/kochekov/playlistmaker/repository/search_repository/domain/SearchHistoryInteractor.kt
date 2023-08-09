package av.kochekov.playlistmaker.repository.search_repository.domain

import av.kochekov.playlistmaker.repository.track_list_repository.models.Track

interface SearchHistoryInteractor {
    fun add(track: Track)

    fun get() : List<Track>

    fun clear()
}