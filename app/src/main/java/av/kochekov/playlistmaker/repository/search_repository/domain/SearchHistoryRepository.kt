package av.kochekov.playlistmaker.repository.search_repository.domain

import av.kochekov.playlistmaker.repository.track_list_repository.models.Track

interface SearchHistoryRepository {

    fun setTrackList(list: List<Track>)

    fun getTrackList() : List<Track>

    fun clear();
}