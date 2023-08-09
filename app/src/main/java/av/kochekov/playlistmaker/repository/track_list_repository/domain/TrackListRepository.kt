package av.kochekov.playlistmaker.repository.track_list_repository.domain

import av.kochekov.playlistmaker.repository.track_list_repository.models.Track

interface TrackListRepository {
    fun search(query: String) : List<Track>?
}