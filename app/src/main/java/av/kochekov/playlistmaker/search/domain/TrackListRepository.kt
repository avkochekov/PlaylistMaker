package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.search.data.model.Track

interface TrackListRepository {
    fun search(query: String): List<Track>?
}