package av.kochekov.playlistmaker.domain.search.api

import av.kochekov.playlistmaker.domain.search.model.Track

interface TrackListRepositoryInterface {
    fun search(query: String) : List<Track>?
}