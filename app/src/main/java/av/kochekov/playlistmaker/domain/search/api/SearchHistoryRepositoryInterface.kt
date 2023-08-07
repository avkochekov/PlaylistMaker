package av.kochekov.playlistmaker.domain.search.api

import av.kochekov.playlistmaker.domain.search.model.Track

interface SearchHistoryRepositoryInterface {

    fun setTrackList(list: List<Track>)

    fun getTrackList() : List<Track>

    fun clear();
}